package com.example.jejugudgo.domain.course.search.elastic.query;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.example.jejugudgo.domain.course.common.dto.MapCoordinate;
import com.example.jejugudgo.domain.course.common.dto.RoutePoint;
import com.example.jejugudgo.domain.course.common.enums.CourseType;
import com.example.jejugudgo.domain.course.search.dto.request.CourseSearchRequest;
import com.example.jejugudgo.domain.course.search.dto.response.CourseSearchResponse;
import com.example.jejugudgo.domain.mygudgo.like.dto.response.LikeInfo;
import com.example.jejugudgo.domain.mygudgo.like.util.UserLikeUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TextSearchQueryService {
    private final ElasticsearchClient elasticsearchClient;
    private final UserLikeUtil userLikeUtil;

    private static final String JEJU_GUDGO = CourseType.COURSE_TYPE01.getType();
    private static final String OLLE = CourseType.COURSE_TYPE02.getType();
    private static final String TRAIL = CourseType.COURSE_TYPE03.getType();
    private static final String ALL = CourseType.COURSE_TYPE04.getType();

    private static final String INDEX_JEJU = "jejugudgo_course";
    private static final String INDEX_OLLE = "olle_course";
    private static final String INDEX_TRAIL = "trail";

    public List<CourseSearchResponse> getCourses(HttpServletRequest httpRequest, CourseSearchRequest request) {
        try {
            BoolQuery.Builder boolQueryBuilder = QueryBuilders.bool();

            // 키워드 조건 추가
            if (request.keyword() != null && !request.keyword().isBlank()) {
                addKeywordQueries(boolQueryBuilder, request.keyword(), request.cat1());
            }

            // 카테고리 1 조건 추가
            if (request.cat1() != null && !request.cat1().equalsIgnoreCase(ALL)) {
                addCategory1Filter(boolQueryBuilder, request.cat1());
            }

            // 카테고리 2 조건 추가
            if (request.cat2() != null && !request.cat2().isEmpty()) {
                addCategory2Filter(boolQueryBuilder, request.cat1(), request.cat2());
            }

            // 카테고리 3 조건 추가
            if (request.cat3() != null && !request.cat3().isEmpty()) {
                addCategory3Filter(boolQueryBuilder, request.cat3());
            }

            SearchRequest searchRequest = createSearchRequest(boolQueryBuilder);
            return executeSearch(httpRequest, searchRequest, request);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addKeywordQueries(BoolQuery.Builder boolQueryBuilder, String keyword, String cat1) {
        boolQueryBuilder.should(QueryBuilders.match(m -> m
                .field("title")
                .query(keyword)
                .boost(6.0f)
        ));

        boolQueryBuilder.should(QueryBuilders.queryString(qs -> qs
                .query("*" + keyword + "*") // 와일드카드 검색
                .defaultField("title")
                .boost(5.0f)
        ));

        boolQueryBuilder.should(QueryBuilders.match(m -> m
                .field("tags")
                .query(keyword)
                .boost(3.0f)
        ));

        if (cat1.equalsIgnoreCase(ALL)) {
            BoolQuery.Builder trailQueryBuilder = QueryBuilders.bool();
            trailQueryBuilder.should(QueryBuilders.match(m -> m
                    .field("title")
                    .query(keyword)
                    .boost(6.0f)
            ));

            trailQueryBuilder.should(QueryBuilders.match(m -> m
                    .field("tags")
                    .query(keyword)
                    .boost(5.0f)
            ));

            BoolQuery.Builder nonTrailQueryBuilder = QueryBuilders.bool();
            nonTrailQueryBuilder.should(QueryBuilders.match(m -> m
                    .field("title")
                    .query(keyword)
                    .boost(6.0f)
            ));

            nonTrailQueryBuilder.should(QueryBuilders.match(m -> m
                    .field("tags")
                    .query(keyword)
                    .boost(3.0f)
            ));

            nonTrailQueryBuilder.should(QueryBuilders.nested(n -> n
                    .path("spots")
                    .query(QueryBuilders.match(m -> m
                            .field("spots.title")
                            .query(keyword)
                            .boost(5.0f)
                    ))
            ));

            boolQueryBuilder.should(QueryBuilders.bool(b -> b
                    .filter(QueryBuilders.terms(t -> t
                            .field("_index")
                            .terms(TermsQueryField.of(tf -> tf.value(List.of(FieldValue.of(INDEX_TRAIL)))))
                    ))
                    .must(trailQueryBuilder.build()._toQuery())
            ));

            boolQueryBuilder.should(QueryBuilders.bool(b -> b
                    .filter(QueryBuilders.terms(t -> t
                            .field("_index")
                            .terms(TermsQueryField.of(tf -> tf.value(List.of(
                                    FieldValue.of(INDEX_JEJU),
                                    FieldValue.of(INDEX_OLLE)
                            ))))
                    ))
                    .must(nonTrailQueryBuilder.build()._toQuery())
            ));
        }

        if (cat1.equalsIgnoreCase(JEJU_GUDGO) || cat1.equalsIgnoreCase(OLLE)) {
            boolQueryBuilder.should(QueryBuilders.nested(n -> n
                    .path("spots")
                    .query(QueryBuilders.match(m -> m
                            .field("spots.title")
                            .query(keyword)
                            .boost(5.0f)
                    ))
            ));
        }

        boolQueryBuilder.minimumShouldMatch("1");
    }

    private void addCategory1Filter(BoolQuery.Builder boolQueryBuilder, String cat1) {
        List<FieldValue> indices = getIndicesByCar1(cat1).stream()
                .map(FieldValue::of)
                .collect(Collectors.toList());

        boolQueryBuilder.filter(QueryBuilders.terms(t -> t
                .field("_index")
                .terms(TermsQueryField.of(tf -> tf.value(indices)))
        ));
    }

    private void addCategory2Filter(BoolQuery.Builder boolQueryBuilder, String cat1, List<String> cat2) {
        String field = getCat2FieldByCat1(cat1);
        if (field != null) {
            List<FieldValue> mappedCat2 = cat2.stream()
                    .map(FieldValue::of)
                    .collect(Collectors.toList());

            if (!mappedCat2.isEmpty()) {
                boolQueryBuilder.filter(QueryBuilders.terms(t -> t
                        .field(field)
                        .terms(TermsQueryField.of(tf -> tf.value(mappedCat2)))
                ));
            }
        }
    }

    private void addCategory3Filter(BoolQuery.Builder boolQueryBuilder, List<String> cat3) {
        List<FieldValue> cat3Values = cat3.stream()
                .map(FieldValue::of)
                .collect(Collectors.toList());

        boolQueryBuilder.filter(QueryBuilders.terms(t -> t
                .field("category3")
                .terms(TermsQueryField.of(tf -> tf.value(cat3Values)))
        ));
    }

    private SearchRequest createSearchRequest(BoolQuery.Builder boolQueryBuilder) {
        return SearchRequest.of(sr -> sr
                .index(List.of(INDEX_JEJU, INDEX_OLLE, INDEX_TRAIL))
                .query(q -> q.bool(boolQueryBuilder.build()))
        );
    }

    private List<CourseSearchResponse> executeSearch(HttpServletRequest httpRequest, SearchRequest searchRequest, CourseSearchRequest request) throws Exception {
        SearchResponse<Map> response = elasticsearchClient.search(searchRequest, Map.class);
        return response.hits().hits().stream()
                .map(hit -> {
                    Map<String, Object> source = hit.source();
                    String index = hit.index();

                    Long id = source.get("id") instanceof Integer
                            ? ((Integer) source.get("id")).longValue()
                            : (Long) source.get("id");

                    List<String> tags = parseTags(source.get("tags"));

                    LikeInfo likeInfo = userLikeUtil.isLiked(httpRequest, request.cat1(), id);
                    
                    String pinkey = "";
                    String courseType = getCourseType(index);
                    if (courseType.equals(JEJU_GUDGO))
                        pinkey = "jeju" + id;
                    else if (courseType.equals(OLLE))
                        pinkey = "olle" + id;
                    else if (courseType.equals(TRAIL))
                        pinkey = "trail" + id;

                    // RoutePoint 시작/끝 지점
                    RoutePoint startPoint = null;
                    RoutePoint endPoint = null;
                    if (!index.equals(INDEX_TRAIL)) {
                        if (source.containsKey("spots") && source.get("spots") instanceof List spots) {
                            List<Map<String, Object>> spotList = (List<Map<String, Object>>) spots;
                            if (!spotList.isEmpty()) {
                                Map<String, Object> startSpot = spotList.get(0);
                                Map<String, Object> endSpot = spotList.get(spotList.size() - 1);

                                startPoint = new RoutePoint(
                                        (String) startSpot.get("title"),
                                        (Double) startSpot.get("latitude"),
                                        (Double) startSpot.get("longitude")
                                );

                                endPoint = new RoutePoint(
                                        (String) endSpot.get("title"),
                                        (Double) endSpot.get("latitude"),
                                        (Double) endSpot.get("longitude")
                                );
                            }
                        }
                    } else {
                        startPoint = new RoutePoint(
                                (String) source.get("title"),
                                (Double) source.get("latitude"),
                                (Double) source.get("longitude")
                        );
                    }

                    return new CourseSearchResponse(
                            id,
                            courseType,
                            tags,
                            likeInfo,
                            (String) source.get("title"),
                            (String) source.get("address"),
                            (String) source.get("route"),
                            (String) source.get("summary"),
                            (String) source.get("content"),
                            (String) source.get("distance"),
                            (String) source.get("time"),
                            (String) source.get("thumbnailUrl"),
                            source.get("starAvg") instanceof Double ? (Double) source.get("starAvg") : null,
                            source.get("reviewCount") instanceof Long ? (Long) source.get("reviewCount") : null,
                            null,
                            null,
                            null,
                            isValid(request, startPoint) ? startPoint : null,
                            endPoint,
                            pinkey
                    );
                })
                .filter(filter -> filter.startPoint() != null)
                .toList();
    }

    private List<String> getIndicesByCar1(String cat1) {
        if (cat1 == null || cat1.equalsIgnoreCase(ALL))
            return List.of(INDEX_JEJU, INDEX_OLLE, INDEX_TRAIL);

        else if (cat1.equalsIgnoreCase(JEJU_GUDGO))
            return List.of(INDEX_JEJU);

        else if (cat1.equalsIgnoreCase(OLLE))
            return List.of(INDEX_OLLE);

        else if (cat1.equalsIgnoreCase(TRAIL))
            return List.of(INDEX_TRAIL);

        return new ArrayList<>();
    }

    private Boolean isValid(CourseSearchRequest request, RoutePoint startPoint) {
        MapCoordinate minCoordinate = request.coordinate().get(0);
        MapCoordinate maxCoordinate = request.coordinate().get(1);

        return startPoint.latitude() >= minCoordinate.latitude()
                && startPoint.latitude() <= maxCoordinate.latitude()
                && startPoint.longitude() >= minCoordinate.longitude()
                && startPoint.longitude() <= maxCoordinate.longitude();
    }

    private String getCourseType(String index) {
        if (index.equals(INDEX_JEJU))
            return JEJU_GUDGO;

        else if (index.equals(INDEX_OLLE))
            return OLLE;

        else if (index.equals(INDEX_TRAIL))
            return TRAIL;

        return null;
    }

    private String getCat2FieldByCat1(String cat1) {
        if (cat1.equals(OLLE))
            return "olleType";
        else
            return "tags";
    }

    private List<String> parseTags(Object tagsObj) {
        if (tagsObj instanceof List) {
            return ((List<?>) tagsObj).stream()
                    .map(Object::toString)
                    .map(tag -> tag
                            .replaceAll("^\\{title=", "")
                            .replaceAll("}$", ""))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }
}

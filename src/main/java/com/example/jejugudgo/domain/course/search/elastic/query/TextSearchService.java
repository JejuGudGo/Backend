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
import com.example.jejugudgo.domain.course.search.service.SearchService;
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
public class TextSearchService implements SearchService {

    private final ElasticsearchClient elasticsearchClient;
    private final UserLikeUtil userLikeUtil;

    private static final String JEJU_GUDGO = CourseType.COURSE_TYPE01.getType();
    private static final String OLLE = CourseType.COURSE_TYPE02.getType();
    private static final String TRAIL = CourseType.COURSE_TYPE03.getType();
    private static final String ALL = CourseType.COURSE_TYPE04.getType();

    private static final String INDEX_JEJU = "jejugudgo_course";
    private static final String INDEX_OLLE = "olle_course";
    private static final String INDEX_TRAIL = "trail";

    @Override
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
                addCategory2Filter(boolQueryBuilder, request.cat2());
            }

            // 카테고리 3 조건 추가
            if (request.cat3() != null && !request.cat3().isEmpty()) {
                addCategory3Filter(boolQueryBuilder, request.cat3());
            }

            SearchRequest searchRequest = createSearchRequest(boolQueryBuilder);
            return executeSearch(httpRequest, searchRequest, request);

        } catch (Exception e) {
            return List.of();
        }
    }

    private void addKeywordQueries(BoolQuery.Builder boolQueryBuilder, String keyword, String cat1) {
        // 공통 조건
        boolQueryBuilder.should(QueryBuilders.match(m -> m
                .field("title")
                .query(keyword)
                .boost(6.0f) // "title" 필드에 높은 가중치 부여
        ));

        boolQueryBuilder.should(QueryBuilders.matchPhrase(m -> m
                .field("title")
                .query(keyword)
                .boost(5.0f) // "title" 내 정확한 구문 검색
        ));

        boolQueryBuilder.should(QueryBuilders.match(m -> m
                .field("tags")
                .query(keyword)
                .boost(4.0f) // "tags" 필드도 검색 가능
        ));

        boolQueryBuilder.should(QueryBuilders.queryString(q -> q
                .query("*" + keyword + "*")
                .defaultField("title")
                .boost(3.0f) // 부분 매칭을 허용
        ));

        if ("전체".equalsIgnoreCase(cat1)) {
            BoolQuery.Builder trailQueryBuilder = QueryBuilders.bool();
            trailQueryBuilder.should(QueryBuilders.match(m -> m
                    .field("title")
                    .query(keyword)
                    .boost(6.0f) // "title" 필드에 높은 가중치 부여
            ));

            trailQueryBuilder.should(QueryBuilders.match(m -> m
                    .field("title")
                    .query(keyword)
                    .boost(5.0f) // "title" 필드에 높은 가중치 부여
            ));

            trailQueryBuilder.should(QueryBuilders.match(m -> m
                    .field("tags")
                    .query(keyword)
                    .boost(4.0f) // "tags" 필드도 검색 가능
            ));

            trailQueryBuilder.should(QueryBuilders.queryString(q -> q
                    .query("*" + keyword + "*")
                    .defaultField("title")
                    .boost(3.0f) // 부분 매칭을 허용
            ));

            BoolQuery.Builder nonTrailQueryBuilder = QueryBuilders.bool();
            nonTrailQueryBuilder.should(QueryBuilders.match(m -> m
                    .field("title")
                    .query(keyword)
                    .boost(6.0f) // "title" 필드에 높은 가중치 부여
            ));

            nonTrailQueryBuilder.should(QueryBuilders.matchPhrase(m -> m
                    .field("title")
                    .query(keyword)
                    .boost(5.0f) // "title" 내 정확한 구문 검색
            ));

            nonTrailQueryBuilder.should(QueryBuilders.match(m -> m
                    .field("tags")
                    .query(keyword)
                    .boost(4.0f) // "tags" 필드도 검색 가능
            ));

            nonTrailQueryBuilder.should(QueryBuilders.queryString(q -> q
                    .query("*" + keyword + "*")
                    .defaultField("title")
                    .boost(3.0f) // 부분 매칭을 허용
            ));

            nonTrailQueryBuilder.should(QueryBuilders.nested(n -> n
                    .path("spots")
                    .query(QueryBuilders.match(m -> m
                            .field("spots.title")
                            .query(keyword)
                            .boost(5.0f)
                    ))
            ));

            // Trail 인덱스
            boolQueryBuilder.should(QueryBuilders.bool(b -> b
                    .filter(QueryBuilders.terms(t -> t
                            .field("_index")
                            .terms(TermsQueryField.of(tf -> tf.value(List.of(FieldValue.of(INDEX_TRAIL)))))
                    ))
                    .must(trailQueryBuilder.build()._toQuery())
            ));

            // Non-trail 인덱스
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

    private void addCategory2Filter(BoolQuery.Builder boolQueryBuilder, List<String> cat2) {
        List<FieldValue> cat2Values = cat2.stream()
                .map(FieldValue::of)
                .collect(Collectors.toList());

        boolQueryBuilder.filter(QueryBuilders.terms(t -> t
                .field("tags")
                .terms(TermsQueryField.of(tf -> tf.value(cat2Values)))
        ));
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

                    Long id = source.get("id") instanceof Integer
                            ? ((Integer) source.get("id")).longValue()
                            : (Long) source.get("id");

                    List<String> tags = source.get("tags") instanceof List
                            ? (List<String>) source.get("tags")
                            : new ArrayList<>();

                    LikeInfo likeInfo = userLikeUtil.isLiked(httpRequest, request.cat1(), id);

                    // RoutePoint 시작/끝 지점
                    RoutePoint startPoint = null;
                    RoutePoint endPoint = null;
                    if (!source.get("_index").equals(INDEX_TRAIL)) {
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

                    assert startPoint != null;
                    return new CourseSearchResponse(
                            id,
                            request.cat1(),
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
                            source.get("likeCount") instanceof Long ? (Long) source.get("likeCount") : null,
                            source.get("clickCount") instanceof Long ? (Long) source.get("clickCount") : null,
                            source.get("upToDate") instanceof Double ? (Double) source.get("upToDate") : null,
                            isValid(request, startPoint) ? startPoint : null,
                            endPoint
                    );
                })
                .filter(filter -> filter.startPoint() != null)
                .collect(Collectors.toList());
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

        if (startPoint.latitude() >= minCoordinate.latitude() && startPoint.latitude() <= maxCoordinate.latitude() && startPoint.longitude() >= minCoordinate.longitude() && startPoint.longitude() <= maxCoordinate.longitude())
            return true;
        return false;
    }
}

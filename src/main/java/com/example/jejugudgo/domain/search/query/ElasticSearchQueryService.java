package com.example.jejugudgo.domain.search.query;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.example.jejugudgo.domain.course.jejugudgo.entity.CourseTag;
import com.example.jejugudgo.domain.course.olle.entity.OlleType;
import com.example.jejugudgo.domain.review.enums.ReviewCategory3;
import com.example.jejugudgo.domain.search.dto.SearchListResponse;
import com.example.jejugudgo.domain.trail.entity.TrailType;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ElasticSearchQueryService {
    private final ElasticsearchClient elasticsearchClient;

    public ElasticSearchQueryService(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    public List<SearchListResponse> searchCoursesByKeywordAndCategory(String keyword, String cat1, List<String> cat2, List<String> cat3, Pageable pageable) {
        try {
            BoolQuery.Builder boolQueryBuilder = QueryBuilders.bool();

            if (keyword != null && !keyword.isBlank()) {
                addKeywordQueries(boolQueryBuilder, keyword);
            }

            if (cat1 != null && !cat1.equalsIgnoreCase("전체")) {
                addCategory1Filter(boolQueryBuilder, cat1);
            }

            if (cat2 != null && !cat2.isEmpty()) {
                addCategory2Filter(boolQueryBuilder, cat1, cat2);
            }

            if (cat3 != null && !cat3.isEmpty()) {
                addCategory3Filter(boolQueryBuilder, cat3);
            }

            SearchRequest searchRequest = SearchRequest.of(sr -> sr
                    .index(List.of("jeju_gudgo_course", "jeju_olle_course", "trail"))
                    .query(q -> q.functionScore(fs -> fs
                            .query(boolQueryBuilder.build()._toQuery())
                            .functions(fn -> fn
                                    .filter(f -> f.term(t -> t.field("title").value(FieldValue.of(keyword)))).weight(2.0)
                            ).functions(fn -> fn
                                    .filter(f -> f.term(t -> t.field("tags").value(FieldValue.of(keyword)))).weight(1.8)
                            ).functions(fn -> fn
                                    .filter(f -> f.term(t -> t.field("content").value(FieldValue.of(keyword)))).weight(1.6)
                            )
                            .boostMode(FunctionBoostMode.Multiply)
                            .scoreMode(FunctionScoreMode.Sum)
                    ))
                    .from(pageable.getPageNumber() * pageable.getPageSize())
                    .size(pageable.getPageSize())
                    .sort(s -> s
                            .field(f -> f
                                    .field("id").order(SortOrder.Asc)
                                    .field("title").order(SortOrder.Asc)
                            )
                    )
            );

            return executeSearch(searchRequest);

        } catch (Exception e) {
            throw new CustomException(RetCode.RET_CODE99);
        }
    }

    private void addKeywordQueries(BoolQuery.Builder boolQueryBuilder, String keyword) {
        boolQueryBuilder.must(QueryBuilders.multiMatch(m -> m
                .fields(List.of("title^6.0"))
                .query(keyword)
        ));

        boolQueryBuilder.should(QueryBuilders.multiMatch(m -> m
                .fields(List.of("tags^2.0", "time^1.0"))
                .query(keyword)
        ));

        boolQueryBuilder.should(QueryBuilders.nested(n -> n
                .path("spots")
                .query(QueryBuilders.match(m -> m
                        .field("spots.title")
                        .query(keyword)
                        .boost(4.0f)
                ))
        ));
    }

    private void addCategory1Filter(BoolQuery.Builder boolQueryBuilder, String cat1) {
        List<String> indices = getIndicesByCat1(cat1);

        List<FieldValue> fieldValues = indices.stream()
                .map(FieldValue::of)
                .collect(Collectors.toList());

        boolQueryBuilder.filter(QueryBuilders.terms(t -> t
                .field("_index")
                .terms(TermsQueryField.of(tf -> tf.value(fieldValues)))
        ));
    }

    private void addCategory2Filter(BoolQuery.Builder boolQueryBuilder, String cat1, List<String> cat2) {
        String field = getCat2FieldByCat1(cat1);
        if (field != null) {
            List<FieldValue> mappedCat2 = cat2.stream()
                    .map(value -> FieldValue.of(mapCat2ToFieldValue(cat1, value)))
                    .filter(mapped -> mapped != null)
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
        List<FieldValue> mappedCat3 = cat3.stream()
                .map(ReviewCategory3::fromQuery)
                .filter(category -> category != null)
                .map(category -> FieldValue.of(category.getCategory3()))
                .collect(Collectors.toList());

        if (!mappedCat3.isEmpty()) {
            boolQueryBuilder.filter(QueryBuilders.terms(t -> t
                    .field("category3")
                    .terms(TermsQueryField.of(tf -> tf.value(mappedCat3)))
            ));
        }
    }

    private List<SearchListResponse> executeSearch(SearchRequest searchRequest) throws Exception {
        SearchResponse<Map> response = elasticsearchClient.search(searchRequest, Map.class);

        return response.hits().hits().stream()
                .map(hit -> {
                    Map<String, Object> source = hit.source();
                    Long id = null;

                    if (source.get("id") instanceof Integer)
                        id = ((Integer) source.get("id")).longValue();
                    else if (source.get("id") instanceof Long)
                        id = (Long) source.get("id");

                    return new SearchListResponse(
                            id,
                            (String) source.get("type"),
                            (List<String>) source.get("tags"),
                            source.get("isBookmarked") != null && (Boolean) source.get("isBookmarked"),
                            (String) source.get("title"),
                            (String) source.get("summary"),
                            (String) source.get("distance"),
                            (String) source.get("time"),
                            source.get("starAvg") != null ? (Double) source.get("starAvg") : 0.0,
                            source.get("reviewCount") != null ? (Integer) source.get("reviewCount") : 0,
                            (String) source.get("startSpotTitle"),
                            source.get("startSpotLatitude") != null ? (Double) source.get("startSpotLatitude") : 0.0,
                            source.get("startSpotLongitude") != null ? (Double) source.get("startSpotLongitude") : 0.0
                    );
                })
                .collect(Collectors.toList());
    }

    private String mapCat2ToFieldValue(String cat1, String value) {
        switch (cat1) {
            case "제주걷고":
                return CourseTag.fromTag(value).getTag();
            case "제주올레":
                return OlleType.fromType(value).getType();
            case "산책로":
                return TrailType.fromQuery(value).getCode();
            default:
                return null;
        }
    }

    private List<String> getIndicesByCat1(String cat1) {
        if (cat1 == null || cat1.equalsIgnoreCase("전체")) {
            return List.of("jeju_gudgo_course", "jeju_olle_course", "trail");
        }
        switch (cat1) {
            case "제주걷고":
                return List.of("jeju_gudgo_course");
            case "제주올레":
                return List.of("jeju_olle_course");
            case "산책로":
                return List.of("trail");
            default:
                throw new CustomException(RetCode.RET_CODE94);
        }
    }

    private String getCat2FieldByCat1(String cat1) {
        if (cat1 == null || cat1.equalsIgnoreCase("전체")) {
            return null;
        }

        switch (cat1) {
            case "제주걷고", "산책로":
                return "tags";
            case "제주올레":
                return "type";
            default:
                throw new CustomException(RetCode.RET_CODE94);
        }
    }
}
package com.example.jejugudgo.domain.search.query;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.example.jejugudgo.domain.review.util.ReviewCounter;
import com.example.jejugudgo.domain.search.dto.SearchListResponse;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.entity.Bookmark;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.entity.BookmarkType;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.util.BookmarkUtil;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ElasticSearchQueryService {
    private final BookmarkUtil bookmarkUtil;
    private final ReviewCounter reviewCounter;
    private final ElasticsearchClient elasticsearchClient;

    public ElasticSearchQueryService(BookmarkUtil bookmarkUtil, ElasticsearchClient elasticsearchClient, ReviewCounter reviewCounter) {
        this.bookmarkUtil = bookmarkUtil;
        this.elasticsearchClient = elasticsearchClient;
        this.reviewCounter = reviewCounter;
    }

    public List<SearchListResponse> searchCoursesByKeywordAndCategory(HttpServletRequest request, String keyword, String cat1, List<String> cat2, List<String> cat3, Pageable pageable) {
        try {
            BoolQuery.Builder boolQueryBuilder = QueryBuilders.bool();

            // 키워드 조건 추가
            if (keyword != null && !keyword.isBlank()) {
                addKeywordQueries(boolQueryBuilder, keyword, cat1);
            }

            // 카테고리 1 조건 추가
            if (cat1 != null && !cat1.equalsIgnoreCase("전체")) {
                addCategory1Filter(boolQueryBuilder, cat1);
            }

            // 카테고리 2 조건 추가
            if (cat2 != null && !cat2.isEmpty()) {
                addCategory2Filter(boolQueryBuilder, cat1, cat2);
            }

            // 카테고리 3 조건 추가
            if (cat3 != null && !cat3.isEmpty()) {
                addCategory3Filter(boolQueryBuilder, cat3);
            }

            // 쿼리 요청 생성
            SearchRequest searchRequest = SearchRequest.of(sr -> sr
                    .index(List.of("jeju_gudgo_course", "jeju_olle_course", "trail"))
                    .query(q -> q.bool(boolQueryBuilder.build()))
                    .from(pageable.getPageNumber() * pageable.getPageSize())
                    .size(pageable.getPageSize())
                    .sort(List.of(
                            SortOptions.of(s -> s
                                    .field(f -> f
                                            .field("titleKeyword") // 타이틀 정렬
                                            .order(SortOrder.Asc)
                                    )
                            ),
                            SortOptions.of(s -> s
                                    .field(f -> f
                                            .field("starAvg") // 평점 기준 정렬
                                            .order(SortOrder.Desc)
                                    )
                            ),
                            SortOptions.of(s -> s
                                    .field(f -> f
                                            .field("id") // 고유 ID 기준 정렬
                                            .order(SortOrder.Asc)
                                    )
                            )
                    ))
            );

            System.out.println("searchRequest: \n" + searchRequest);
            return executeSearch(request, searchRequest);

        } catch (Exception e) {
            throw new CustomException(RetCode.RET_CODE99);
        }
    }

    private void addKeywordQueries(BoolQuery.Builder boolQueryBuilder, String keyword, String cat1) {
        // 타이틀 조건
        boolQueryBuilder.should(QueryBuilders.match(m -> m
                .field("title")
                .query(keyword)
                .boost(6.0f)
        ));

        boolQueryBuilder.should(QueryBuilders.queryString(w -> w
                .query("*" + keyword + "*") // 부분 일치를 허용
                .defaultField("titleKeyword")
                .boost(6.0f)
        ));

        // 태그 조건
        boolQueryBuilder.should(QueryBuilders.match(m -> m
                .field("tags")
                .query(keyword)
                .boost(3.0f)
        ));

        // "전체"일 때
        if ("전체".equalsIgnoreCase(cat1)) {
            // trail 인덱스를 위한 별도 쿼리
            BoolQuery.Builder trailQueryBuilder = QueryBuilders.bool();
            trailQueryBuilder.should(QueryBuilders.match(m -> m
                    .field("title")
                    .query(keyword)
                    .boost(6.0f)
            ));

            trailQueryBuilder.should(QueryBuilders.match(m -> m
                    .field("tag")
                    .query(keyword)
                    .boost(5.0f)
            ));

            // trail이 아닌 인덱스용 쿼리
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

            nonTrailQueryBuilder.should(QueryBuilders.match(m -> m
                    .field("time")
                    .query(keyword)
                    .boost(1.0f)
            ));

            nonTrailQueryBuilder.should(QueryBuilders.nested(n -> n
                    .path("spots")
                    .query(QueryBuilders.match(m -> m
                            .field("spots.title")
                            .query(keyword)
                            .boost(5.0f)
                    ))
            ));

            // 두 가지 쿼리를 분리하여 추가
            boolQueryBuilder.should(QueryBuilders.bool(b -> b
                    .filter(QueryBuilders.terms(t -> t
                            .field("_index")
                            .terms(TermsQueryField.of(tf -> tf.value(List.of(FieldValue.of("trail")))))
                    ))
                    .must(trailQueryBuilder.build()._toQuery())
            ));

            boolQueryBuilder.should(QueryBuilders.bool(b -> b
                    .filter(QueryBuilders.terms(t -> t
                            .field("_index")
                            .terms(TermsQueryField.of(tf -> tf.value(List.of(
                                    FieldValue.of("jeju_gudgo_course"),
                                    FieldValue.of("jeju_olle_course")
                            ))))
                    ))
                    .must(nonTrailQueryBuilder.build()._toQuery())
            ));
        }

        // 최소 매칭 조건 설정
        boolQueryBuilder.minimumShouldMatch("1");
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
        List<FieldValue> mappedCat3 = cat3.stream()
                .map(FieldValue::of)
                .collect(Collectors.toList());

        if (!mappedCat3.isEmpty()) {
            boolQueryBuilder.filter(QueryBuilders.terms(t -> t
                    .field("category3")
                    .terms(TermsQueryField.of(tf -> tf.value(mappedCat3)))
            ));
        }
    }

    private List<SearchListResponse> executeSearch(HttpServletRequest request, SearchRequest searchRequest) throws Exception {
        SearchResponse<Map> response = elasticsearchClient.search(searchRequest, Map.class);

        return response.hits().hits().stream()
                .map(hit -> {
                    Map<String, Object> source = hit.source();

                    Long id = source.get("id") instanceof Integer
                            ? ((Integer) source.get("id")).longValue()
                            : (Long) source.get("id");

                    List<String> tags = new ArrayList<>();
                    if (source.get("type").equals("산책로")) {
                        tags.add((String) source.get("tag"));

                    } else {
                        tags = (List<String>) source.get("tags");
                    }

                    BookmarkType  bookmarkType = BookmarkType.fromCode((String) source.get("type"));
                    Bookmark bookmark =  bookmarkUtil
                            .isBookmarked(request, bookmarkType, id);
                    Long reviewCount = reviewCounter.getReviewCount(bookmarkType, id);

                    Double starAvg = (Double) source.get("starAvg");
                    starAvg = starAvg == 0.0 ? null : starAvg;

                    return new SearchListResponse(
                            id,
                            (String) source.get("type"),
                            tags,
                            bookmark != null,
                            bookmark != null ? bookmark.getId() : null,
                            (String) source.get("title"),
                            (String) source.get("summary"),
                            (String) source.get("distance"),
                            (String) source.get("time"),
                            (String) source.get("imageUrl"),
                            starAvg,
                            reviewCount,
                            (String) source.get("startSpotTitle"),
                            (Double) source.get("startLatitude"),
                            (Double) source.get("startLongitude")
                    );
                })
                .collect(Collectors.toList());
    }

    private List<String> getIndicesByCat1(String cat1) {
        if (cat1 == null || cat1.equalsIgnoreCase("전체")) {
            return List.of("jeju_gudgo_course", "jeju_olle_course", "trail");
        }
        switch (cat1) {
            case "제주객의 길":
                return List.of("jeju_gudgo_course");
            case "올레길":
                return List.of("jeju_olle_course");
            case "산책로":
                return List.of("trail");
            default:
                throw new CustomException(RetCode.RET_CODE94);
        }
    }

    private String getCat2FieldByCat1(String cat1) {
        switch (cat1) {
            case "제주객의 길":
            case "산책로":
                return "tag";
            case "올레길":
                return "type";
            default:
                return null;
        }
    }
}
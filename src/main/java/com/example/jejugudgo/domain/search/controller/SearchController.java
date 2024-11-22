package com.example.jejugudgo.domain.search.controller;

import com.example.jejugudgo.domain.search.dto.SearchDetailResponse;
import com.example.jejugudgo.domain.search.dto.SearchListResponse;
import com.example.jejugudgo.domain.search.query.ElasticSearchQueryService;
import com.example.jejugudgo.domain.search.query.SearchQueryService;
import com.example.jejugudgo.domain.search.service.SearchService;
import com.example.jejugudgo.global.exception.dto.CommonApiResponse;
import com.example.jejugudgo.global.exception.util.ApiResponseUtil;
import com.example.jejugudgo.global.util.PagingUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/search")
public class SearchController {
    private final ElasticSearchQueryService elasticSearchQueryService;
    private final SearchQueryService searchQueryService;
    private final SearchService searchService;
    private final ApiResponseUtil apiResponseUtil;

    /**
     *
     * @param keyword 검색어
     * @param cat1 올레길, 제주객의 길, 산책로 (미 입력시 전체)
     * @param cat2 cat1 에 맞는 중분류
     * @param cat3 리뷰 목적에 맞는 소분류
     * @param page 페이지
     * @param size 페이지당 사이즈
     */
    @GetMapping(value = "keywords")
    public ResponseEntity<CommonApiResponse> searchCourseByKeywordAndTags(
            HttpServletRequest request,
            @RequestParam(value = "keyword") String keyword,
            @RequestParam(value = "cat1", defaultValue = "전체") String cat1,
            @RequestParam(value = "cat2",  required = false) List<String> cat2,
            @RequestParam(value = "cat3",  required = false) List<String> cat3,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        List<SearchListResponse> responses = elasticSearchQueryService.searchCoursesByKeywordAndCategory(request, keyword, cat1, cat2, cat3, pageable);
        return ResponseEntity.ok(apiResponseUtil.success(responses, "search-result"));
    }

    /**
     * @param latitude 현재 위도
     * @param longitude 현재 경도
     * @param cat1 올레길, 제주객의 길, 산책로 (미 입력시 전체)
     * @param cat2 cat1 에 맞는 중분류
     * @param cat3 리뷰 목적에 맞는 소분류
     */
    @GetMapping(value = "/tags")
    public ResponseEntity<CommonApiResponse> searchCourseByTags(
            HttpServletRequest request,
            @RequestParam(value = "lat", required = false) String latitude,
            @RequestParam(value = "lon", required = false) String longitude,
            @RequestParam(value = "cat1", defaultValue = "전체") String cat1,
            @RequestParam(value = "cat2",  required = false) List<String> cat2,
            @RequestParam(value = "cat3",  required = false) List<String> cat3,
            Pageable pageable
    ) {
        Pageable page = PagingUtil.createPageable(pageable.getPageNumber(), pageable.getPageSize());
        List<SearchListResponse> responses = searchQueryService.searchCoursesBySpotOrCategory(request, cat1, cat2, cat3, latitude, longitude, page);
        return ResponseEntity.ok(apiResponseUtil.success(responses, "search-result"));
    }

    /**
     * @param type 올레길, 제주객의 길,  산책로 (미 입력시 전체)
     * @param id 인덱스
     */

    @GetMapping(value = "/detail")
    public ResponseEntity<CommonApiResponse> getCourseDetail(
            HttpServletRequest request,
            @RequestParam(value = "type", defaultValue = "산책로") String type,
            @RequestParam(value = "id") Long id
    ) {
        SearchDetailResponse response = searchService.getCourseDetail(request, type, id);
        return ResponseEntity.ok(apiResponseUtil.success(response));
    }
}

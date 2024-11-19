package com.example.jejugudgo.domain.search.controller;

import com.example.jejugudgo.domain.search.dto.SearchListResponse;
import com.example.jejugudgo.domain.search.service.SearchElasticService;
import com.example.jejugudgo.global.exception.dto.CommonApiResponse;
import com.example.jejugudgo.global.exception.util.ApiResponseUtil;
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
    private final SearchElasticService searchElasticService;
    private final ApiResponseUtil apiResponseUtil;

    @GetMapping
    public ResponseEntity<CommonApiResponse> searchCourseByKeywordAndTags(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "cat1", defaultValue = "전체") String cat1,
            @RequestParam(value = "cat2",  required = false) List<String> cat2,
            @RequestParam(value = "cat3",  required = false) List<String> cat3,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<SearchListResponse> responses = searchElasticService.searchCourses(keyword, cat1, cat2, cat3, pageable);
        return ResponseEntity.ok(apiResponseUtil.success(responses, "search-result"));
    }
}

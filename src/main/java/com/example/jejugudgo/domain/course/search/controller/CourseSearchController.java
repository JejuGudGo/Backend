package com.example.jejugudgo.domain.course.search.controller;

import com.example.jejugudgo.domain.course.search.dto.response.CourseDetailResponse;
import com.example.jejugudgo.domain.course.search.dto.response.CoursePathResponse;
import com.example.jejugudgo.domain.course.search.dto.response.CourseSearchResponse;
import com.example.jejugudgo.domain.course.search.service.CourseSearchService;
import com.example.jejugudgo.global.exception.dto.CommonApiResponse;
import com.example.jejugudgo.global.exception.util.ApiResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/search")
@RequiredArgsConstructor
public class CourseSearchController {
    private final ApiResponseUtil apiResponseUtil;
    private final CourseSearchService courseSearchService;

    @GetMapping(value = "")
    public ResponseEntity<CommonApiResponse> getCourses(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "cat1", defaultValue = "전체") String cat1,
            @RequestParam(value = "cat2", required = false) List<String> cat2,
            @RequestParam(value = "cat3", required = false) List<String> cat3,
            @RequestParam(value = "coordinates") List<String> coordinates,
            HttpServletRequest httpRequest
    ) {
        List<CourseSearchResponse> responses = courseSearchService.getCourses(httpRequest, keyword, cat1, cat2, cat3, coordinates);
        return ResponseEntity.ok(apiResponseUtil.success(responses, "results"));
    }

    @GetMapping(value = "/detail")
    public ResponseEntity<CommonApiResponse> getCourses(
            @RequestParam(value = "cat1") String cat1,
            @RequestParam(value = "id") String id,
            HttpServletRequest httpRequest
    ) {
        CourseDetailResponse response = courseSearchService.getCourse(httpRequest, cat1, id);
        return ResponseEntity.ok(apiResponseUtil.success(response));
    }

    @GetMapping(value = "/path")
    public ResponseEntity<CommonApiResponse> getCourses(
            @RequestParam(value = "cat1") String cat1,
            @RequestParam(value = "id") String id
    ) {
        CoursePathResponse response = courseSearchService.getCoursePath(cat1, id);
        return ResponseEntity.ok(apiResponseUtil.success(response));
    }
}

package com.example.jejugudgo.domain.course.olle.controller;

import com.example.jejugudgo.domain.course.olle.dto.response.JejuOlleCourseResponseDetail;
import com.example.jejugudgo.domain.course.olle.dto.response.JejuOlleCourseResponseForList;
import com.example.jejugudgo.domain.course.olle.query.JejuOlleCourseQueryService;
import com.example.jejugudgo.global.exception.dto.CommonApiResponse;
import com.example.jejugudgo.global.exception.util.ApiResponseUtil;
import com.example.jejugudgo.global.util.PagingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/courses/olle")
@RequiredArgsConstructor
public class JejuOlleCourseController {

    private final JejuOlleCourseQueryService jejuOlleCourseQueryService;
    private final ApiResponseUtil apiResponseUtil;

    @GetMapping("")
    public ResponseEntity<CommonApiResponse> getJejuOlleCourses(Pageable pageable) {
        Pageable page = PagingUtil.createPageable(pageable.getPageNumber(), pageable.getPageSize());
        List<JejuOlleCourseResponseForList> response = jejuOlleCourseQueryService.getOlleCourses(page);
        return ResponseEntity.ok(apiResponseUtil.success(response));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CommonApiResponse> getJejuOlleDetail(@PathVariable("courseId") Long courseId) {
        JejuOlleCourseResponseDetail response = jejuOlleCourseQueryService.getOlleCourse(courseId);
        return ResponseEntity.ok(apiResponseUtil.success(response));
    }

}

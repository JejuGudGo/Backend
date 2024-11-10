package com.example.jejugudgo.domain.olle.controller;

import com.example.jejugudgo.domain.olle.dto.response.JejuOlleCourseResponseDetail;
import com.example.jejugudgo.domain.olle.dto.response.JejuOlleCourseResponseForList;
import com.example.jejugudgo.domain.olle.query.JejuOlleCourseQueryService;
import com.example.jejugudgo.global.exception.dto.response.CommonApiResponse;
import com.example.jejugudgo.global.util.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/courses/olle")
@RequiredArgsConstructor
public class JejuOlleCourseController {

    private final JejuOlleCourseQueryService jejuOlleCourseQueryService;
    private final ApiResponseUtil apiResponseUtil;

    @GetMapping("")
    public ResponseEntity<CommonApiResponse> getJejuOlleCourses(Pageable pageable) {
        Page<JejuOlleCourseResponseForList> response = jejuOlleCourseQueryService.getOlleCourses(pageable);
        return ResponseEntity.ok(apiResponseUtil.success(response));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CommonApiResponse> getJejuOlleDetail(@PathVariable("courseId") Long courseId) {
        JejuOlleCourseResponseDetail response = jejuOlleCourseQueryService.getOlleCourse(courseId);
        return ResponseEntity.ok(apiResponseUtil.success(response));
    }

}

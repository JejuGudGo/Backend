package com.example.jejugudgo.domain.course.recommend.controller;

import com.example.jejugudgo.domain.course.recommend.dto.OpenApiResponse;
import com.example.jejugudgo.domain.course.recommend.service.RecommendService;
import com.example.jejugudgo.global.exception.dto.CommonApiResponse;
import com.example.jejugudgo.global.exception.util.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/courses/recommend")
public class RecommendController {
    private final ApiResponseUtil apiResponseUtil;
    private final RecommendService recommendService;

    @GetMapping(value = "")
    public ResponseEntity<CommonApiResponse> getRecommends(
            @RequestParam("type") String type,
            @RequestParam(value = "coordinates", required = false) List<String> coordinates,
            @RequestParam(name = "radius", required = false) String radius // 1.0
    ) {
        List<OpenApiResponse> responses = recommendService.getRecommendations(type, coordinates, radius);

        return ResponseEntity.ok(apiResponseUtil.success(responses, "contents"));
    }
}

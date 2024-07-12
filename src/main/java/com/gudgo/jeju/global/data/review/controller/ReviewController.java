package com.gudgo.jeju.global.data.review.controller;

import com.gudgo.jeju.global.data.review.dto.ReviewCategoryResponse;
import com.gudgo.jeju.global.data.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/categories/tags")
    public ResponseEntity<List<ReviewCategoryResponse>> getCategoriesTags() {
        List<ReviewCategoryResponse> responses = reviewService.getCategoryAndTags();

        return ResponseEntity.ok(responses);
    }
}

package com.gudgo.jeju.domain.planner.review.controller;


import com.gudgo.jeju.domain.planner.review.dto.response.PlannerReviewCountResponseDto;
import com.gudgo.jeju.domain.planner.review.dto.response.ReviewImageResponseDto;
import com.gudgo.jeju.domain.planner.review.dto.response.ReviewResponseDto;
import com.gudgo.jeju.domain.planner.review.query.ReviewImageQueryService;
import com.gudgo.jeju.domain.planner.review.query.ReviewQueryService;
import com.gudgo.jeju.domain.planner.review.service.UserReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/planners")
@RequiredArgsConstructor
@Slf4j
@RestController
public class PlannerReviewController {
    private final ReviewQueryService reviewQueryService;
    private final ReviewImageQueryService reviewImageQueryService;
    private final UserReviewService userReviewService;

    // GET - 리뷰 목록
    @GetMapping(value = "/{plannerId}/reviews")
    public Page<ReviewResponseDto> getReviews(
            @PathVariable("plannerId") Long plannerId,
            Pageable pageable) {
        return reviewQueryService.getReviews(plannerId, pageable);
    }

    // GET - 리뷰 이미지 목록
    @GetMapping(value = "/{plannerId}/reviews/images")
    public Page<ReviewImageResponseDto> getImages(
            @PathVariable("plannerId") Long plannerId,
            Pageable pageable) {
        return reviewImageQueryService.getImages(plannerId, pageable);
    }

    // GET - 리뷰 상세
    @GetMapping(value = "/{plannerId}/reviews/{reviewId}")
    public ReviewResponseDto getReview(@PathVariable("reviewId") Long reviewId) {
        return reviewQueryService.getReview(reviewId);
    }

    // GET - 리뷰 이미지 조회
    @GetMapping(value = "/{plannerId}/reviews/{reviewId}/images/{imageId}")
    public ReviewImageResponseDto getImage(@PathVariable("imageId") Long imageId) {
        return reviewImageQueryService.getReviewImage(imageId);
    }

    // GET - 리뷰 개수 조회
    @GetMapping(value = "/{plannerId}/reviews/count")
    public PlannerReviewCountResponseDto getReviewCount(@PathVariable("plannerId") Long plannerId) {
        return reviewQueryService.getReviewCount(plannerId);
    }

    // GET - 리뷰 카테고리 / 태그 반환
    @GetMapping(value = "/reviews/tags")
    public List<?> getReviewTags() {
        return userReviewService.getCategoryAndTags();
    }
}

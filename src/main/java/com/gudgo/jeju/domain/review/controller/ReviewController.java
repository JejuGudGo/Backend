package com.gudgo.jeju.domain.review.controller;

import com.gudgo.jeju.domain.review.dto.request.UserCourseReviewRequest;
import com.gudgo.jeju.domain.review.dto.response.PlannerReviewResponse;
import com.gudgo.jeju.domain.review.dto.response.ReviewResponse;
import com.gudgo.jeju.domain.review.service.ReviewService;
import com.gudgo.jeju.domain.review.query.ReviewQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
@RestController
public class ReviewController {
    private final ReviewQueryService reviewQueryService;
    private final ReviewService reviewService;

    // GET - 특정 유저 리뷰 목록 조회
    @GetMapping(value = "/users/{userId}/reviews")
    public Page<ReviewResponse> getUserReviews(
            @PathVariable("userId") Long userId,
            Pageable pageable) {

        return reviewQueryService.getUserReviews(userId, pageable);
    }

    @GetMapping(value = "/planners/{plannerId}/reviews")
    public ResponseEntity<PlannerReviewResponse> getPlannerReviews(@PathVariable("plannerId") Long plannerId) {
        PlannerReviewResponse response = reviewService.getPlannerReview(plannerId);

        return ResponseEntity.ok(response);
    }

    // 산책로 달성하고 작성하는 리뷰
    @PostMapping(value = "/users/{userId}/tails/{trailId}/reviews")
    public ResponseEntity<?> createTrailReview(
            @PathVariable("trailId") Long trailId,
            @PathVariable("userId") Long userId,
            @RequestPart("request") UserCourseReviewRequest requestDto,
            @RequestPart(value = "image", required = false) MultipartFile[] images) throws Exception {

        reviewService.createTrailReview(trailId, userId, requestDto, images);

        return ResponseEntity.ok().build();
    }

    // 코스 달성하고 작성하는 리뷰
    @PostMapping(value = "/users/{userId}/planners/{plannerId}/reviews")
    public ResponseEntity<?> createUserCourseReview(
            @PathVariable("plannerId") Long plannerId,
            @PathVariable("userId") Long userId,
            @RequestPart("request") UserCourseReviewRequest requestDto,
            @RequestPart(value = "image", required = false) MultipartFile[] images) throws Exception {

        reviewService.createUserCourseReview(plannerId, userId, requestDto, images);

        return ResponseEntity.ok().build();
    }

    // PATCH - 리뷰 수정
    @PatchMapping(value = "/reviews/{reviewId}")
    public ResponseEntity<?> updateReview(
            @PathVariable("reviewId") Long reviewId,
            @RequestPart("request") UserCourseReviewRequest requestDto,
            @RequestPart(value="image", required = false) MultipartFile[] images) throws Exception {

        reviewService.updateCourseReview(reviewId, requestDto, images);

        return ResponseEntity.ok().build();
    }

    // DELETE - 리뷰 삭제
    @DeleteMapping(value = "/reviews/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable("reviewId") Long reviewId) {
        reviewService.deleteCourseReview(reviewId);
        return ResponseEntity.ok().build();
    }
}

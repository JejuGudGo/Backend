package com.gudgo.jeju.domain.review.controller;

import com.gudgo.jeju.domain.review.dto.request.ReviewRequestDto;
import com.gudgo.jeju.domain.review.dto.request.ReviewUpdateRequestDto;
import com.gudgo.jeju.domain.review.dto.response.ReviewPostResponseDto;
import com.gudgo.jeju.domain.review.dto.response.ReviewResponseDto;
import com.gudgo.jeju.domain.review.query.ReviewQueryService;
import com.gudgo.jeju.domain.review.service.UserReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@RestController
public class UserReviewController {
    private final ReviewQueryService reviewQueryService;
    private final UserReviewService userReviewService;

    // GET - 특정 유저 리뷰 목록 조회
    @GetMapping(value = "/{userId}/reviews")
    public Page<ReviewResponseDto> getUserReviews(
            @PathVariable("userId") Long userId,
            Pageable pageable) {
        return reviewQueryService.getUserReviews(userId, pageable);
    }

    // POST - 리뷰 작성
    @PostMapping(value = "/{userId}/planners/{plannerId}/reviews")
    public ResponseEntity<ReviewPostResponseDto> create(
            @PathVariable("plannerId") Long plannerId,
            @PathVariable("userId") Long userId,
            @RequestPart("request") ReviewRequestDto requestDto,
            @RequestPart("image") MultipartFile[] images) throws Exception {

        ReviewPostResponseDto response = userReviewService.create(plannerId, userId, requestDto, images);
        return ResponseEntity.ok(response);

    }

    // PATCH - 리뷰 수정
    @PatchMapping(value = "/{userId}/reviews/{reviewId}")
    public ResponseEntity<?> update(
            @PathVariable("reviewId") Long reviewId,
            @RequestPart("request") ReviewUpdateRequestDto requestDto,
            @RequestPart("image") MultipartFile[] images) throws Exception {
        ReviewPostResponseDto response = userReviewService.update(reviewId, requestDto, images);
        return ResponseEntity.ok(response);

    }

    // DELETE - 리뷰 삭제
    @DeleteMapping(value = "/{userId}/reviews/{reviewId}")
    public ResponseEntity<?> delete(@PathVariable("reviewId") Long reviewId) {
        userReviewService.delete(reviewId);
        return ResponseEntity.ok().build();
    }
}

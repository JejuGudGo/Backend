package com.gudgo.jeju.domain.review.controller;


import com.gudgo.jeju.domain.review.dto.request.ReviewRequestDto;
import com.gudgo.jeju.domain.review.dto.response.PlannerReviewCountResponseDto;
import com.gudgo.jeju.domain.review.dto.response.ReviewImageResponseDto;
import com.gudgo.jeju.domain.review.dto.response.ReviewPostResponseDto;
import com.gudgo.jeju.domain.review.dto.response.ReviewResponseDto;
import com.gudgo.jeju.domain.review.query.ReviewImageQueryService;
import com.gudgo.jeju.domain.review.query.ReviewQueryService;
import com.gudgo.jeju.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
@Slf4j
@RestController
public class ReviewController {
    private final ReviewQueryService reviewQueryService;
    private final ReviewService reviewService;
    private final ReviewImageQueryService reviewImageQueryService;

    // GET - 리뷰 목록
    @GetMapping(value="/list/{plannerId}")
    public Page<ReviewResponseDto> getReviews(
            @PathVariable("plannerId") Long plannerId,
            Pageable pageable) {
        return reviewQueryService.getReviews(plannerId, pageable);
    }

    // GET - 리뷰 이미지 목록
    @GetMapping(value = "/images/{plannerId}")
    public Page<ReviewImageResponseDto> getImages(
            @PathVariable("plannerId") Long plannerId,
            Pageable pageable) {
        return reviewImageQueryService.getImages(plannerId, pageable);
    }


    // GET - 리뷰 상세
    @GetMapping(value = "/{reviewId}")
    public ReviewResponseDto getReview(@PathVariable("reviewId") Long reviewId) {
        return reviewQueryService.getReview(reviewId);
    }

    // GET - 리뷰 이미지 조회
    @GetMapping(value = "/image/{imageId}")
    public ReviewImageResponseDto getImage(@PathVariable("imageId") Long imageId) {
        return reviewImageQueryService.getReviewImage(imageId);
    }

    // GET - 리뷰 개수 조회
    @GetMapping(value = "/{plannerId}/count")
    public PlannerReviewCountResponseDto getReviewCount(@PathVariable("plannerId") Long plannerId) {
        return reviewQueryService.getReviewCount(plannerId);
    }


    // POST - 리뷰 작성
    @PostMapping(value = "/{plannerId}/{userId}")
    public ResponseEntity<ReviewPostResponseDto> create(
            @PathVariable("plannerId") Long plannerId,
            @PathVariable("userId") Long userId,
            @RequestPart("request") ReviewRequestDto requestDto,
            @RequestPart("image") MultipartFile[] images) throws Exception {

        ReviewPostResponseDto response = reviewService.create(plannerId, userId, requestDto, images);
        return ResponseEntity.ok(response);

    }





}

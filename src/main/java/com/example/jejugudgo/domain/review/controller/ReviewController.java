package com.example.jejugudgo.domain.review.controller;

import com.example.jejugudgo.domain.review.dto.request.ReviewCreateRequest;
import com.example.jejugudgo.domain.review.service.ReviewService;
import com.example.jejugudgo.global.exception.dto.CommonApiResponse;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.exception.util.ApiResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ApiResponseUtil apiResponseUtil;
    private final ReviewService reviewService;

    /**
     * @param type 코스 종류(jejugudgo, olle, trail)
     */
    @PostMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonApiResponse> createOlleCourseReview(
            @RequestParam("type") String type,
            @PathVariable("id") Long courseId,
            @RequestPart("request") ReviewCreateRequest request,
            @RequestPart("images") List<MultipartFile> images,
            HttpServletRequest httpServletRequest
    ) throws Exception {

        reviewService.createReview(type, courseId, request, images, httpServletRequest);

        return ResponseEntity.ok(apiResponseUtil.success(null));
    }

    // TODO: 리뷰 수정, 삭제
}

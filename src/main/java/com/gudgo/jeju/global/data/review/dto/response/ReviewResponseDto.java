package com.gudgo.jeju.global.data.review.dto.response;

import com.gudgo.jeju.global.data.review.entity.ReviewTag;

import java.time.LocalDate;
import java.util.List;

public record ReviewResponseDto(
        Long reviewId,
        String title, // planner
        String oriUserNickname, // planner
        LocalDate createdAt, // planner
        String userProfileImg, // 작성자
        Long avgStars,
        List<ReviewTag> reviewTags, // review
        String content, // review
        List<String> images // review image
) {
}

package com.gudgo.jeju.global.data.review.dto.response;

import com.gudgo.jeju.global.data.review.entity.ReviewFilterTag;

import java.time.LocalDate;
import java.util.List;

public record ReviewResponse(
        Long reviewId,
        String title, // planner
        String nickname, // planner
        LocalDate createdAt, // planner
        String userProfileImg, // 작성자
        double avgStars,
        List<ReviewFilterTag> reviewTags, // review
        String content, // review
        List<String> images // review image
) {
}

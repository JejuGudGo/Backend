package com.gudgo.jeju.domain.planner.review.dto.response;

import java.time.LocalDate;
import java.util.List;

public record ReviewPostResponseDto(
        Long reviewId,
        Long plannerId,
        String content,
        double stars,

        LocalDate createdAt,

        List<ReviewImageResponseDto> images,
        List<ReviewCategoryResponseDto> reviewCategories

) {
}

package com.gudgo.jeju.domain.planner.review.dto.response;

import java.time.LocalDate;
import java.util.List;

public record ReviewResponseDto(
        Long id,
        Long plannerId,
        Long userId,
        String content,
        LocalDate createdAt,
        Long stars,
        List<ReviewImageResponseDto> reviewImages,
        List<ReviewCategoryResponseDto> reviewCategories


) {
}

package com.gudgo.jeju.domain.review.dto.response;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

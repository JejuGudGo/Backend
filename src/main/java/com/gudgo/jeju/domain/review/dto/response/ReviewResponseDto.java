package com.gudgo.jeju.domain.review.dto.response;

import java.util.List;

public record ReviewResponseDto(
        Long id,
        Long plannerId,
        String content,
        List<ReviewImageResponseDto> reviewImages,

        List<ReviewCategoryResponseDto> reviewCategories
) {
}

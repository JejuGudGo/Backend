package com.gudgo.jeju.domain.review.dto.response;

import java.util.List;

public record ReviewCategoryResponseDto(
        Long id,
        Long plannerReviewId,
        String code,
//        String title,
        List<ReviewTagResponseDto> plannerReviewTags
) {
}

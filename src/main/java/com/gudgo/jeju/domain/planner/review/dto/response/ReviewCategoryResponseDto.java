package com.gudgo.jeju.domain.planner.review.dto.response;

import java.util.List;

public record ReviewCategoryResponseDto(
        Long id,
        Long plannerReviewId,
        String code,
//        String title,
        List<ReviewTagResponseDto> plannerReviewTags
) {
}

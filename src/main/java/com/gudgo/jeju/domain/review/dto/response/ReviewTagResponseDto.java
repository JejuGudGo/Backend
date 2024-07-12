package com.gudgo.jeju.domain.review.dto.response;

import com.gudgo.jeju.domain.review.entity.PlannerReviewCategory;

public record ReviewTagResponseDto(
        Long id,
        Long categoryId,
        String code
//        String title
) {
}

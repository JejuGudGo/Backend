package com.gudgo.jeju.domain.planner.review.dto.response;

public record ReviewImageResponseDto(
        Long id,
        Long plannerReviewId,
        String imageUrl
) {
}

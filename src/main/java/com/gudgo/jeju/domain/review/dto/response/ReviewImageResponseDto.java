package com.gudgo.jeju.domain.review.dto.response;

public record ReviewImageResponseDto(
        Long id,
        Long plannerReviewId,
        String imageUrl
) {
}

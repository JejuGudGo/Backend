package com.gudgo.jeju.domain.review.dto.response;

public record OlleCourseSpotResponseDto(
        String title,
        double latitude,
        double longitude,
        String distance
) {
}

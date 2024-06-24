package com.gudgo.jeju.domain.olle.dto.response;

public record OlleCourseSpotResponseDto(
        String title,
        double latitude,
        double longitude,
        String distance
) {
}

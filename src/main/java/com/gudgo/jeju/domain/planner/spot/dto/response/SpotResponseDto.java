package com.gudgo.jeju.domain.planner.spot.dto.response;

public record SpotResponseDto(
        Long id,
        String categoryId,
        Long courseId,
        String title,
        Long order,
        String address,
        double latitude,
        double longitude,
        boolean isCompleted,
        Long count
) {
}

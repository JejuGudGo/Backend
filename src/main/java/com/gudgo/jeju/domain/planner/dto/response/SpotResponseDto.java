package com.gudgo.jeju.domain.planner.dto.response;

public record SpotResponseDto(
        Long id,
        String subContentId,
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
package com.gudgo.jeju.domain.course.dto.response;

public record SpotResponseDto(
        Long id,
        Long courseId,
        String categoryId,
        String title,
        Long order,
        String address,
        double latitude,
        double longitude,
        boolean isDeleted,
        boolean isCompleted,
        Long count

) {
}

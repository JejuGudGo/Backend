package com.gudgo.jeju.domain.course.dto.request;

public record SpotCreateRequestDto(
        Long courseId,
        String categoryId,
        String title,
        Long order,
        String address,
        double latitude,
        double longitude

) {
}

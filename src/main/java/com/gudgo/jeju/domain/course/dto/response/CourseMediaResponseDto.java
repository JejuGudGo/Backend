package com.gudgo.jeju.domain.course.dto.response;

public record CourseMediaResponseDto(
        Long id,
        Long courseId,
        String imageUrl,
        String coutent,
        double latitude,
        double longitude
) {
}

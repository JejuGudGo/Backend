package com.gudgo.jeju.domain.planner.courseMedia.dto.response;

public record CourseMediaResponseDto(
        Long id,
        Long courseId,
        String imageUrl,
        String coutent,
        double latitude,
        double longitude
) {
}

package com.gudgo.jeju.domain.planner.courseMedia.dto.response;

public record CourseMediaBackImagesResponseDto(
        Long mediaId,
        String backImageUrl,
        double latitude,
        double longitude
) {
}

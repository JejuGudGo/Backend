package com.gudgo.jeju.domain.planner.courseMedia.dto.response;

public record CourseMediaResponseDto(
        Long mediaId,
//        String imageUrl,
        String selfImageUrl,
        String backImageUrl,
        String content,
        double latitude,
        double longitude
) {
}

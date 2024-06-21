package com.gudgo.jeju.domain.course.dto.request;

import jakarta.validation.constraints.Size;

public record CourseMediaCreateRequestDto(
        Long courseId,
        String imageUrl,

        @Size(max = 100)  // 최대 100자로 제한
        String content,
        double latitude,
        double longitude
) {
}

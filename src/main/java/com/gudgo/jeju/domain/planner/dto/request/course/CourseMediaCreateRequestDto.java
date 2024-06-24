package com.gudgo.jeju.domain.planner.dto.request.course;

import jakarta.validation.constraints.Size;

public record CourseMediaCreateRequestDto(
        String imageUrl,
        @Size(max = 100)  // 최대 100자로 제한
        String content,
        double latitude,
        double longitude
) {
}

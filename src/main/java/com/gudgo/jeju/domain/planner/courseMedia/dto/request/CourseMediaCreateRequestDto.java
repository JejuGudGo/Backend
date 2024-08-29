package com.gudgo.jeju.domain.planner.courseMedia.dto.request;

import jakarta.validation.constraints.Size;

public record CourseMediaCreateRequestDto(
        @Size(max = 100)  // 최대 100자로 제한
        String content,
        double latitude,
        double longitude
) {
}

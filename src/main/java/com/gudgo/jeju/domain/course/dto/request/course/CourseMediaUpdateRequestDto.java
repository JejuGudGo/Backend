package com.gudgo.jeju.domain.course.dto.request.course;

import jakarta.validation.constraints.Size;

public record CourseMediaUpdateRequestDto(
        @Size(max = 100)  // 최대 100자로 제한
        String content
) {
}

package com.gudgo.jeju.domain.course.dto.request.course;

import java.time.LocalDate;

public record CourseUpdateRequestDto(
        String title,
        LocalDate startAt
) {
}

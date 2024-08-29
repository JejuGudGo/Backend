package com.gudgo.jeju.domain.planner.course.dto.request;

import java.time.LocalDate;

public record CourseCreateRequestDto(
        String title,
        LocalDate startAt
) {
}

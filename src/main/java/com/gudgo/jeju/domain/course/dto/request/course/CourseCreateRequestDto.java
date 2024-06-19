package com.gudgo.jeju.domain.course.dto.request.course;

import org.springframework.cglib.core.Local;

import java.time.LocalTime;

public record CourseCreateRequestDto(
        String title,
        String summary,
        int distance
) {
}

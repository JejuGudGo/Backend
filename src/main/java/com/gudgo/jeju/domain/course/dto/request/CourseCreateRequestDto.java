package com.gudgo.jeju.domain.course.dto.request;

import org.springframework.cglib.core.Local;

import java.time.LocalTime;

public record CourseCreateRequestDto(
        String title,
        LocalTime time,
        String summary
) {
}

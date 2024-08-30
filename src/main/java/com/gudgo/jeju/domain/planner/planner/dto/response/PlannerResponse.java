package com.gudgo.jeju.domain.planner.planner.dto.response;

import com.gudgo.jeju.domain.planner.course.dto.response.CourseResponseDto;

import java.time.LocalDate;
import java.time.LocalTime;

public record PlannerResponse(
        Long plannerId,
        LocalDate startAt,
        String summary,
        LocalTime time,
        boolean isCompleted,
        CourseResponseDto course
) {
}

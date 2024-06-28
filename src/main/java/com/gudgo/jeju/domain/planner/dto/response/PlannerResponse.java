package com.gudgo.jeju.domain.planner.dto.response;

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

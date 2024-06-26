package com.gudgo.jeju.domain.planner.dto.request.course;

import java.time.LocalDate;
import java.time.LocalTime;

public record PlannerUpdateRequestDto (
        LocalDate startAt,
        boolean isPrivate,
        String summary,
        LocalTime time,
        boolean isCompleted
) {
}

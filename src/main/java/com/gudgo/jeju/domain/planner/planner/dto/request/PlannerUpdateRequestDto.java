package com.gudgo.jeju.domain.planner.planner.dto.request;

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

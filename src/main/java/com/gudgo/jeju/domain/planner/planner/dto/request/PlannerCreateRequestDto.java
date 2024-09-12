package com.gudgo.jeju.domain.planner.planner.dto.request;

import java.time.LocalDate;

public record PlannerCreateRequestDto(
        String title,
        LocalDate startAt,
        String summary,
        Long originalCreatorId,
        Long originalCourseId,
        boolean isPrivate
) {
}

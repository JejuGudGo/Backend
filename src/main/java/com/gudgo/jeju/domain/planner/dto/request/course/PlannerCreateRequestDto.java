package com.gudgo.jeju.domain.planner.dto.request.course;

import java.time.LocalDate;

public record PlannerCreateRequestDto(
        String title,
        LocalDate startAt,
        Long originalCreatorId,
        Long originalCourseId,
        boolean isPrivate
) {
}

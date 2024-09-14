package com.gudgo.jeju.domain.planner.planner.dto.request;

import com.gudgo.jeju.domain.planner.label.dto.request.LabelRequestDto;

import java.time.LocalDate;

public record PlannerCreateRequestDto(
        String title,
        LocalDate startAt,
        String summary,
        Long originalCreatorId,
        Long originalCourseId,
        boolean isPrivate,
        LabelRequestDto labelRequestDto
) {
}

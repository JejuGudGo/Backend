package com.gudgo.jeju.domain.course.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record PlanResponseDto(
        Long id,
        Long userId,
        String title,
        LocalTime time,
        LocalDate StartAt,
        LocalDate createdAt,
        boolean isCompleted,
        boolean isDeleted,
        Long originalCreatorId,
        Long originalCourseId
) {
}

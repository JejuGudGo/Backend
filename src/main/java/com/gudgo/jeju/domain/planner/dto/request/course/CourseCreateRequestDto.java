package com.gudgo.jeju.domain.planner.dto.request.course;

import com.gudgo.jeju.domain.planner.entity.CourseType;

import java.time.LocalDate;

public record CourseCreateRequestDto(
        String title,
        LocalDate startAt,
        Long originalCreatorId,
        Long originalCourseId,
        boolean isPrivate

) {
}

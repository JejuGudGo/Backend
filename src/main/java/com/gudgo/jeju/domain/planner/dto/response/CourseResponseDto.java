package com.gudgo.jeju.domain.planner.dto.response;

import com.gudgo.jeju.domain.planner.entity.QSpot;
import com.gudgo.jeju.domain.planner.entity.Spot;

import java.time.LocalDate;
import java.time.LocalTime;

public record CourseResponseDto(
        Long id,
        String title,
        LocalTime time,
        LocalDate StartAt,
        LocalDate createdAt,
        boolean isDeleted,
        Long originalCreatorId,
        Long originalCourseId,
        String summary,

        Spot spot
) {
}
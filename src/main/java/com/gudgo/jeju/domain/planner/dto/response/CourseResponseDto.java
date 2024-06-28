package com.gudgo.jeju.domain.planner.dto.response;

import com.gudgo.jeju.domain.planner.entity.CourseType;
import com.gudgo.jeju.domain.planner.entity.QSpot;
import com.gudgo.jeju.domain.planner.entity.Spot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record CourseResponseDto(
        Long id,
        CourseType type,
        String title,
        LocalDate createdAt,
        Long originalCreatorId,
        Long originalCourseId,
        Long olleCourseId,
        List<SpotResponseDto> spots
) {
}

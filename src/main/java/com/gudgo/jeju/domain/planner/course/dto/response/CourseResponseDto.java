package com.gudgo.jeju.domain.planner.course.dto.response;

import com.gudgo.jeju.domain.planner.spot.dto.response.SpotResponseDto;
import com.gudgo.jeju.domain.planner.course.entity.CourseType;

import java.time.LocalDate;
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

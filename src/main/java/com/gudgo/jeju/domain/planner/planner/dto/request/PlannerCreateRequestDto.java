package com.gudgo.jeju.domain.planner.planner.dto.request;

import com.gudgo.jeju.domain.planner.course.dto.request.CourseCreateRequestDto;
import com.gudgo.jeju.domain.planner.spot.dto.request.SpotCreateRequestDto;
import com.gudgo.jeju.domain.planner.tag.entity.PlannerType;

import java.time.LocalDate;
import java.util.List;

public record PlannerCreateRequestDto(
        String title,
        LocalDate startAt,
        String summary,
        Long originalCreatorId,
        Long originalCourseId,
        boolean isPrivate,
        List<PlannerType> tags,
        CourseCreateRequestDto courseCreateRequestDto,
        List<SpotCreateRequestDto> spotCreateRequestDto
) { }

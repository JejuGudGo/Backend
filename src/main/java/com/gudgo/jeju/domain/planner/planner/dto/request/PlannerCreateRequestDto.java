package com.gudgo.jeju.domain.planner.planner.dto.request;

import com.gudgo.jeju.domain.planner.course.dto.request.CourseCreateRequestDto;
import com.gudgo.jeju.domain.planner.spot.dto.request.SpotCreateRequestDto;
import com.gudgo.jeju.domain.planner.tag.entity.PlannerType;

import java.util.List;

public record PlannerCreateRequestDto(
        String title,
        boolean isPrivate,
        List<PlannerType> tags,
        CourseCreateRequestDto courseCreateRequestDto,
        List<SpotCreateRequestDto> spotCreateRequestDto
) { }

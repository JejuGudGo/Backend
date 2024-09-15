package com.gudgo.jeju.domain.planner.planner.dto.response;

import com.gudgo.jeju.domain.planner.course.dto.response.CourseResponseDto;
import com.gudgo.jeju.domain.planner.tag.entity.PlannerTag;
import com.gudgo.jeju.domain.planner.tag.entity.PlannerType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record PlannerResponse(
        Long plannerId,
        LocalDate startAt,
        String summary,
        LocalTime time,
        boolean isCompleted,
        List<PlannerTagResponse> tags,
        CourseResponseDto course
) {
}

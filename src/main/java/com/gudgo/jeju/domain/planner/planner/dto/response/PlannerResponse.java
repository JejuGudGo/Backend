package com.gudgo.jeju.domain.planner.planner.dto.response;

import com.gudgo.jeju.domain.planner.course.dto.response.CourseResponseDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record PlannerResponse(
        Long plannerId,
        LocalDate startAt,
        String summary,
        LocalTime time,
        boolean isCompleted,
        List<PlannerTagResponse> labelCode,
        CourseResponseDto course
) {
}

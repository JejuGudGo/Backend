package com.gudgo.jeju.domain.planner.course.dto.request;

import com.gudgo.jeju.domain.planner.course.entity.CourseType;


public record CourseCreateRequestDto(
        CourseType type,
        String title,
        Long userId,
        String imageUrl,
        String content
) {
}

package com.gudgo.jeju.domain.course.dto.request;

import com.gudgo.jeju.domain.course.entity.CourseType;

public record SpotCreateRequestDto(
        Long courseId,
        String categoryId,
        String title,
        CourseType courseType,
        Long order,
        String address,
        double latitude,
        double longitude

) {
}

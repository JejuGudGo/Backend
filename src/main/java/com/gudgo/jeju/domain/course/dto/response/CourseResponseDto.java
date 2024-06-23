package com.gudgo.jeju.domain.course.dto.response;

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
        String summary
) {
}

package com.gudgo.jeju.domain.course.dto.request;

import java.sql.Time;
import java.time.LocalDate;

public record PlanCreateRequestDto(
        LocalDate startAt,
        Time time,
        Long originalCourseId,
        String summary){
}

package com.gudgo.jeju.domain.course.dto.request.plan;

import java.sql.Time;
import java.time.LocalDate;

public record PlanCreateRequestDto(
        LocalDate startAt,
        String title,
        Time time,
        Long originalCourseId,
        String summary,
        String olleType){

}
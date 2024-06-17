package com.gudgo.jeju.domain.course.dto.request;

import java.time.LocalDate;

public record PlanCreateRequestDto(LocalDate startAt, Long originalCourseId){
}

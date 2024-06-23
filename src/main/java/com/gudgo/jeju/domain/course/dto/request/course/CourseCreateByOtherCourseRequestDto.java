package com.gudgo.jeju.domain.course.dto.request.course;

import java.time.LocalDate;

public record CourseCreateByOtherCourseRequestDto(
        LocalDate startAt,
        String title
){
}

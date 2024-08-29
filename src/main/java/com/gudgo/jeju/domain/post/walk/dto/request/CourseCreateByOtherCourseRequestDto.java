package com.gudgo.jeju.domain.post.walk.dto.request;

import java.time.LocalDate;

public record CourseCreateByOtherCourseRequestDto(
        LocalDate startAt,
        String title
){
}

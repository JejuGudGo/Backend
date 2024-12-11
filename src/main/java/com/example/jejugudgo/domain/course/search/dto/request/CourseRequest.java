package com.example.jejugudgo.domain.course.search.dto.request;

import com.example.jejugudgo.domain.course.common.enums.CourseType;

public record CourseRequest(
        CourseType cat1,
        Long id
) {
}

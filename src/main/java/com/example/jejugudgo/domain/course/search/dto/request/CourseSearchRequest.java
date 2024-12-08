package com.example.jejugudgo.domain.course.search.dto.request;

import com.example.jejugudgo.domain.course.common.dto.MapCoordinate;
import jakarta.annotation.Nullable;

public record CourseSearchRequest(
        @Nullable String keyword,
        String cat1,
        @Nullable String cat2,
        @Nullable String cat3,
        MapCoordinate coordinate
) {
}

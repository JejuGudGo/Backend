package com.example.jejugudgo.domain.course.search.dto.request;

import com.example.jejugudgo.domain.course.common.dto.MapCoordinate;
import jakarta.annotation.Nullable;

import java.util.List;

public record CourseSearchRequest(
        @Nullable String keyword,
        String cat1,
        @Nullable List<String> cat2,
        @Nullable List<String> cat3,
        List<MapCoordinate> coordinate
) {
}

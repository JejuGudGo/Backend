package com.example.jejugudgo.domain.course.common.dto.response;

import jakarta.annotation.Nullable;

import java.util.List;

public record CourseListResponse(
        @Nullable Long id,
        List<String> tags,
        @Nullable String title,
        @Nullable String route,
        @Nullable String distance,
        @Nullable String time,
        String thumbnailUrl,
        @Nullable Double starAvg,
        @Nullable Long reviewCount
) {
}

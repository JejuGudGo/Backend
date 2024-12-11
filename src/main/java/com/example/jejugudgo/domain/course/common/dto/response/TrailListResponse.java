package com.example.jejugudgo.domain.course.common.dto.response;

import com.example.jejugudgo.domain.course.common.dto.RoutePoint;
import jakarta.annotation.Nullable;

import java.util.List;

public record TrailListResponse(
        @Nullable Long id,
        String tag,
        @Nullable String title,
        String content,
        String thumbnailUrl,
        @Nullable Double starAvg,
        @Nullable Long reviewCount
) {
}

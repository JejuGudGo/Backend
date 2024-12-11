package com.example.jejugudgo.domain.course.recommend.dto;

import com.example.jejugudgo.domain.course.recommend.enums.ContentType;
import jakarta.annotation.Nullable;

public record OpenApiRequest(
        @Nullable ContentType type,
        @Nullable Double latitude,
        @Nullable Double longitude,
        @Nullable Double radius
) {
}

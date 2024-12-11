package com.example.jejugudgo.domain.course.recommend.dto;

import jakarta.annotation.Nullable;

public record OpenApiResponse(
    @Nullable String thumbnailUrl,
    String title,
    String address,
    double latitude,
    double longitude
) {
}

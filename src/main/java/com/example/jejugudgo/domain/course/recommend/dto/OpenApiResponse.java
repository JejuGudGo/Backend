package com.example.jejugudgo.domain.course.recommend.dto;

public record OpenApiResponse(
    String thumbnailUrl,
    String title,
    String address,
    String latitude,
    String longitude
) {
}

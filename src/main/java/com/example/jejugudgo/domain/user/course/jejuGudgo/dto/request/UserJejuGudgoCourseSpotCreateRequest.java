package com.example.jejugudgo.domain.user.course.jejuGudgo.dto.request;

public record UserJejuGudgoCourseSpotCreateRequest(
        Long jejuGudgoSpotId,
        String title,
        String spotType,
        Long orderNumber,
        String address,
        double latitude,
        double longitude
) {
}

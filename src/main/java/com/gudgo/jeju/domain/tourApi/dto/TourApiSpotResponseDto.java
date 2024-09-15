package com.gudgo.jeju.domain.tourApi.dto;

public record TourApiSpotResponseDto(
        String id,
        String updatedAt,
        double latitude,
        double longitude,
        String title,
        String imageUrl,
        String content
) {
}

package com.gudgo.jeju.domain.tourApi.dto;

public record TourApiSpotResponseDto(
        String id,
        String categoryId,
        String updatedAt,
        double latitude,
        double longitude,
        String title
) {

}

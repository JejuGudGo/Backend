package com.gudgo.jeju.domain.planner.spot.dto.request;

public record SpotCreateRequestDto(
        String title,
        String address,
        String category,
        double latitude,
        double longitude
) {
}

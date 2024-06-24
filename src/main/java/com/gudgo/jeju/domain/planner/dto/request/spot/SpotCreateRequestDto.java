package com.gudgo.jeju.domain.planner.dto.request.spot;

public record SpotCreateRequestDto(
        String title,
        String address,
        double latitude,
        double longitude
) {
}

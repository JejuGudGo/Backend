package com.gudgo.jeju.domain.planner.spot.dto.response;

public record SpotCreateResponse(
        Long id,
        String title,
        double latitude,
        double longitude
) {
}

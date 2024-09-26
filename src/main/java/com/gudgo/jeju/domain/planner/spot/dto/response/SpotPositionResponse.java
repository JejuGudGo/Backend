package com.gudgo.jeju.domain.planner.spot.dto.response;

public record SpotPositionResponse(
        Long spotId,
        Long order,
        String title,
        double latitude,
        double longitude,
        String distance
) {
}

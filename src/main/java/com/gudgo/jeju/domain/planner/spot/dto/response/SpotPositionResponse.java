package com.gudgo.jeju.domain.planner.spot.dto.response;

public record SpotPositionResponse(
        Long order,
        double latitude,
        double longitude
) {
}

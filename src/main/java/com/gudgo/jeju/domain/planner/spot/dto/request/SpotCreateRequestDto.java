package com.gudgo.jeju.domain.planner.spot.dto.request;

import com.gudgo.jeju.domain.planner.spot.entity.SpotType;

public record SpotCreateRequestDto(
        String title,
        String address,
        String category,
        SpotType type,
        String contentTypeId,
        double latitude,
        double longitude
) {
}

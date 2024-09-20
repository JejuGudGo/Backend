package com.gudgo.jeju.domain.oreum.dto;

import com.gudgo.jeju.domain.tourApi.entity.TourApiCategory1;
import com.gudgo.jeju.domain.trail.entity.TrailType;

public record OreumResponseDto(
        Long id,
        TrailType trailType,
        String title,
        String address,
        double latitude,
        double longitude,
        String content
) {
}

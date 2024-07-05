package com.gudgo.jeju.global.data.oruem.dto;

import com.gudgo.jeju.domain.tourApi.entity.TourApiCategory1;

public record OreumResponseDto(
        Long id,
        TourApiCategory1 tourApiCategory1,
        String title,
        String address,
        double latitude,
        double longitude,
        String content
) {
}

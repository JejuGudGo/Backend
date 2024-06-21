package com.gudgo.jeju.global.data.tourAPI.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gudgo.jeju.global.data.tourAPI.spot.dto.TourApiSpotDto;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TourApiSubContentTypeDto(
        double latitude,
        double longitude,
        String updatedAt,
        TourApiSpotDto tourApiSpotDto
) {
}

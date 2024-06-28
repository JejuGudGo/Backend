package com.gudgo.jeju.domain.tourApi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gudgo.jeju.domain.tourApi.dto.TourApiSpotDto;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TourApiContentDto(
        double latitude,
        double longitude,
        String updatedAt,
        TourApiSpotDto tourApiSpotDto
) {
}

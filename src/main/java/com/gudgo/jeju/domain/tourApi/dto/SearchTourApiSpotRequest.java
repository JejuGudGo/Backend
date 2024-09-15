package com.gudgo.jeju.domain.tourApi.dto;

public record SearchTourApiSpotRequest (
        double latitude,
        double longitude,
        String contentTypeId
) {
}

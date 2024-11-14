package com.example.jejugudgo.domain.olle.dto.response;

public record JejuOlleSpotResponse(
        Long jejuOlleSpotId,
        String title,
        double latitude,
        double longitude,
        Long spotOrder
) {
}

package com.example.jejugudgo.domain.course.olle.dto.response;

public record JejuOlleSpotResponse(
        Long jejuOlleSpotId,
        String title,
        double latitude,
        double longitude,
        Long spotOrder
) {
}

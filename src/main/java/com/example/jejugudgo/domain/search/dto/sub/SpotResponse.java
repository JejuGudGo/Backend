package com.example.jejugudgo.domain.search.dto.sub;

public record SpotResponse(
        Long id,
        String title,
        Long order,
        double latitude,
        double longitude
) {
}

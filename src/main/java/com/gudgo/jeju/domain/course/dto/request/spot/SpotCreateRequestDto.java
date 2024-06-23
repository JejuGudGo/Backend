package com.gudgo.jeju.domain.course.dto.request.spot;

public record SpotCreateRequestDto(
        String title,
        String address,
        double latitude,
        double longitude
) {
}

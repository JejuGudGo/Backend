package com.gudgo.jeju.domain.olle.dto.response;

import com.gudgo.jeju.domain.olle.entity.OlleType;

import java.util.List;

public record OlleCourseDetailResponseDto(
        Long id,
        OlleType olleType,
        String courseNumber,
        String title,
        double startLatitude,
        double startLongitude,
        double endLatitude,
        double endLongitude,
        List<OlleCourseSpotResponseDto> olleSpots
) {
}

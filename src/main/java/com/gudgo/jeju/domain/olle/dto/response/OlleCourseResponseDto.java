package com.gudgo.jeju.domain.olle.dto.response;

import com.gudgo.jeju.domain.olle.entity.OlleType;

public record OlleCourseResponseDto(
        long id,

        OlleType olleType,

        String title,

        double startLatitude,

        double startLongitude,

        double endLatitude,

        double endLongitude,

        boolean wheelchairAccessible,

        String totalDistance,

        String totalTime
) {
}

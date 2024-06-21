package com.gudgo.jeju.domain.course.dto.response;

import com.gudgo.jeju.global.data.olle.entity.OlleType;

public record OlleCourseResponseDto(
        long id,

        OlleType olleType,

        String courseNumber,

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

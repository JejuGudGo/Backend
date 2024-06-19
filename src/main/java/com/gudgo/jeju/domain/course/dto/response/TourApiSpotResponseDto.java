package com.gudgo.jeju.domain.course.dto.response;

import java.sql.Time;
import java.time.LocalTime;

public record TourApiSpotResponseDto(
        String id,
        String categoryId,
        String updatedAt,
        double latitude,
        double longitude

) {

}

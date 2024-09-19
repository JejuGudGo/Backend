package com.gudgo.jeju.domain.search.dto.response;

import java.time.LocalTime;
import java.util.List;

public record SearchListResponse(
        String type,
        Long id,
        String imgUrl,
        String title,
        String summary,
        String distance,
        LocalTime timeLabs,
        double startLatitude,
        double startLongitude,
        double avgStars,
        Long reviewCount,
        List<String> tags
) {
}

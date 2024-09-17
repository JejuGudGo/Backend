package com.gudgo.jeju.domain.trail.dto.response;

import com.gudgo.jeju.domain.trail.entity.TrailType;

import java.util.List;

public record TrailListResponse(
        String title,
        String summary,
        double avgStars,
        Long reviewCount,
        List<TrailType> tags
) {
}

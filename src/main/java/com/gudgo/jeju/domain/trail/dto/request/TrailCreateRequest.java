package com.gudgo.jeju.domain.trail.dto.request;

import com.gudgo.jeju.domain.trail.entity.TrailType;

import java.util.List;

public record TrailCreateRequest(
        String title,
        String summary,
        String address,
        String time,
        String content,
        String tel,
        List<TrailType> trailTypes
) {
}

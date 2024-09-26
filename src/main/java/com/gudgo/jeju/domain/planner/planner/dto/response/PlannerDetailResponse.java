package com.gudgo.jeju.domain.planner.planner.dto.response;

import com.gudgo.jeju.domain.planner.planner.entity.PlannerType;
import com.gudgo.jeju.domain.planner.spot.dto.response.SpotPositionResponse;

import java.time.LocalTime;
import java.util.List;

public record PlannerDetailResponse(
        Long plannerId,
        Long courseId,
        String title,
        String summary,
        String distance,
        LocalTime time,
        double avgStar,
        Long reviewCount,
        List<PlannerType> tags,
        List<SpotPositionResponse> spots
) {
}

package com.gudgo.jeju.domain.planner.planner.dto.response;

import java.time.LocalTime;
import java.util.List;

public record PlannerListResponse(
        Long plannerId,
        String summary,
        String distance,
        LocalTime time,
        double avgStar,
        Long reviewCount,
        boolean isCompleted,
        boolean isPrivate,
        List<String> tags
) {
}

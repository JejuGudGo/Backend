package com.gudgo.jeju.domain.planner.planner.dto.response;

import com.gudgo.jeju.domain.planner.planner.entity.PlannerType;

public record PlannerTagResponse(
        Long id,
        PlannerType code
) {
}

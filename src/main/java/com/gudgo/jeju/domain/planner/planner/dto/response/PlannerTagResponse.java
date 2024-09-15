package com.gudgo.jeju.domain.planner.planner.dto.response;

import com.gudgo.jeju.domain.planner.tag.entity.PlannerType;

public record PlannerTagResponse(
        Long id,
        PlannerType code
) {
}

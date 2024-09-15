package com.gudgo.jeju.domain.planner.planner.dto.response;

import com.gudgo.jeju.domain.planner.spot.dto.response.SpotCreateResponse;
import com.gudgo.jeju.domain.planner.tag.entity.PlannerType;

import java.util.List;

public record PlannerCreateResponse(
        String title,
        String content,
        List<PlannerType> tags,
        List<SpotCreateResponse> spots
) { }

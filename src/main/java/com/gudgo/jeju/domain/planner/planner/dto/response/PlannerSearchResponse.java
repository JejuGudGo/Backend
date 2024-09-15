package com.gudgo.jeju.domain.planner.planner.dto.response;

import java.time.LocalTime;
import java.util.List;

public record PlannerSearchResponse (
    Long plannerId,
    String title,
    String content,
    String distance,
    LocalTime time,
    Long reviewCount,
    double starAvg,
    List<PlannerTagResponse> tags
){ }

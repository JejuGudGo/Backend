package com.gudgo.jeju.domain.planner.planner.dto.response;

import com.gudgo.jeju.domain.user.dto.UserInfoResponseDto;

import java.sql.Time;

public record PlannerUserResponse(
        UserInfoResponseDto userInfoResponse,
        Time totalTime,
        Long totalWalkingCount,
        Long totalBadge
) {
}

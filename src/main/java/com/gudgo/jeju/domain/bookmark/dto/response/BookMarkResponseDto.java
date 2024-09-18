package com.gudgo.jeju.domain.bookmark.dto.response;

import com.gudgo.jeju.domain.planner.planner.dto.response.PlannerListResponse;
import com.gudgo.jeju.domain.trail.dto.response.TrailListResponse;
import com.gudgo.jeju.domain.user.dto.UserInfoResponseDto;

public record BookMarkResponseDto(
        Long bookmarkId,
        PlannerListResponse plannerListResponse,
        TrailListResponse trailListResponse
) {
}

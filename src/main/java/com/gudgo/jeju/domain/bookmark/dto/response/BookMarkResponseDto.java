package com.gudgo.jeju.domain.bookmark.dto.response;

import com.gudgo.jeju.domain.planner.planner.dto.response.PlannerResponse;
import com.gudgo.jeju.domain.user.dto.UserInfoResponseDto;

public record BookMarkResponseDto(
        Long id,
        UserInfoResponseDto userInfoResponseDto,
        PlannerResponse plannerResponse
) {
}

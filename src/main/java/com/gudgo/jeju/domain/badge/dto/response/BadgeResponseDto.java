package com.gudgo.jeju.domain.badge.dto.response;

import com.gudgo.jeju.domain.user.dto.UserInfoResponseDto;

public record BadgeResponseDto(
        Long id,
        UserInfoResponseDto userInfoResponseDto,
        com.gudgo.jeju.domain.badge.entity.BadgeCode code
) {
}

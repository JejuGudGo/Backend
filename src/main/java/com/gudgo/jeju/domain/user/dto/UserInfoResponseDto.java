package com.gudgo.jeju.domain.user.dto;

import com.gudgo.jeju.domain.user.entity.Role;

public record UserInfoResponseDto(
        Long id,
        String email,
        String nickname,
        String name,
        Long numberTag,
        String profileImageUrl,
        Role userRole
) {
}

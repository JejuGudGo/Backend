package com.gudgo.jeju.domain.user.dto;

public record UserInfoResponseDto(
        Long id,
        String email,
        String nickname,
        String name,
        Long numberTag,
        String profileImageUrl
) {
}

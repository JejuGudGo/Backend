package com.gudgo.jeju.domain.user.dto;

public record UserInfoUpdateRequestDto(
        String password,
        String name
) {}

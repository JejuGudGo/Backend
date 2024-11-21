package com.example.jejugudgo.domain.auth.basic.dto.response;

import com.example.jejugudgo.domain.user.user.entity.Role;

import java.time.LocalDateTime;

public record UserInfoResponse(
        Long userId,
        String email,
        String name,
        String nickname,
        LocalDateTime createdAt,
        String phoneNumber,
        Role userRole,
        String profileImage,
        String accessToken
) {

}

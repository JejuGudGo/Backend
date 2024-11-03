package com.example.jejugudgo.domain.user.dto.response;

import com.example.jejugudgo.domain.user.entity.Role;

import java.time.LocalDateTime;

public record UserInfoResponse(
        Long userId,
        String email,
        String name,
        String nickname,
        LocalDateTime createdAt,
        String phoneNumber,
        Role userRole,
        String accessToken,
        String profileImage
) {

}

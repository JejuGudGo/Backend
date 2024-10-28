package com.example.jejugudgo.domain.profile.dto.request;

public record UserProfileUpdateRequest(
        String nickname,
        String profileImageUrl
) {
}

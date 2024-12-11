package com.example.jejugudgo.domain.user.account.dto.response;

import java.time.LocalTime;

public record UserProfileResponse(
        LocalTime walkingTime,
        Long walkingCount,
        String profileImageUrl,
        String nickname,
        Long badgeCount
) {
}

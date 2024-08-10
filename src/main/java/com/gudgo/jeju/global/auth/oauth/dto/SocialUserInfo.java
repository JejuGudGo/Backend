package com.gudgo.jeju.global.auth.oauth.dto;

public record SocialUserInfo(
        String email,
        String name,
        String profileImageUrl
) {
}

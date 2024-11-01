package com.example.jejugudgo.domain.auth.oauth.repository;

public record OAuthRequest(
        String oauthUserId,
        String email,
        String profileImgUrl,
        String nickname
) {
}

package com.example.jejugudgo.domain.user.athentication.signIn.dto.request;

import jakarta.annotation.Nullable;

public record OauthRequest(
    String oauthUserId,
    String email,
    @Nullable String profileImgUrl,
    String nickname
) {
}

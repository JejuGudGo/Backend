package com.example.jejugudgo.domain.user.athentication.signIn.dto.request;

import jakarta.annotation.Nullable;

public record OauthRequest(
    String oauthUserId,
    @Nullable String email,
    @Nullable String profileImgUrl,
    @Nullable String nickname
) {
}

package com.example.jejugudgo.domain.user.athentication.signIn.dto.response;

public record SignInResponse(
    Long userId,
    String email,
    String name,
    String nickname,
    String profileImgUrl,
    String accessToken
) {
}

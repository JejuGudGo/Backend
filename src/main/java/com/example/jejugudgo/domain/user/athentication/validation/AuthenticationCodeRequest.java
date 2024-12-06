package com.example.jejugudgo.domain.user.athentication.validation;

public record AuthenticationCodeRequest(
        String key,
        String value
) {
}

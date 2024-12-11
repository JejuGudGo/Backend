package com.example.jejugudgo.domain.user.athentication.signIn.dto.request;

import com.example.jejugudgo.domain.user.common.enums.Provider;
import jakarta.annotation.Nullable;

public record SignInRequest(
        String email,
        String password,
        @Nullable Provider provider
) {
}

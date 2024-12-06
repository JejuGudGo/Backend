package com.example.jejugudgo.global.security;

import com.example.jejugudgo.domain.user.common.enums.Provider;
import com.example.jejugudgo.domain.user.common.enums.UserStatus;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final Provider provider;

    public CustomAuthenticationToken(Object principal, Object credentials, Provider provider) {
        super(principal, credentials);
        this.provider = provider;
    }

}

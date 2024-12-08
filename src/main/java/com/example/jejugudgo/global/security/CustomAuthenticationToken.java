package com.example.jejugudgo.global.security;

import com.example.jejugudgo.domain.user.common.enums.Provider;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;
    private Object credentials;
    @Getter private Provider provider;

    public CustomAuthenticationToken(Object principal, Object credentials, Provider provider) {
        super(List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.principal = principal;
        this.credentials = credentials;
        this.provider = provider;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}

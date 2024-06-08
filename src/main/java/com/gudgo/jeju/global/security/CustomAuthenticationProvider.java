package com.gudgo.jeju.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final CustomUserDetailsService customUserDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) {
        final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        final String email = token.getName();
        final String password = (String) token.getCredentials();
        final CustomUserDetails userDetails;

        userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(email);
        if (!bCryptPasswordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("AUTH_02");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

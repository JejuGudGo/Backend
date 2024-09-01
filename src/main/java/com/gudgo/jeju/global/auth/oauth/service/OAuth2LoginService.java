package com.gudgo.jeju.global.auth.oauth.service;

import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.auth.oauth.dto.OAuth2LoginRequset;
import com.gudgo.jeju.global.jwt.token.TokenGenerator;
import com.gudgo.jeju.global.jwt.token.TokenType;
import com.gudgo.jeju.global.util.CookieUtil;
import com.gudgo.jeju.global.util.RedisUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2LoginService {
    private final AuthenticationManager authenticationManager;
    private final TokenGenerator tokenGenerator;
    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;

    public void login(OAuth2LoginRequset loginRequest, HttpServletResponse response) {
        User user = userRepository.findByEmailAndProvider(loginRequest.email(), loginRequest.provider())
                .orElseThrow(() -> new UsernameNotFoundException("AUTH_01"));

        if (!user.isDeleted()) {
            addAccessTokenToHeader(user.getId(), response);
            addRefreshTokenToCookidAndRedis(user.getId(), response);

        } else {
            throw new IllegalArgumentException();
        }
    }

    private void addAccessTokenToHeader(Long userId, HttpServletResponse response) {
        String accessToken = tokenGenerator.generateToken(TokenType.ACCESS, String.valueOf(userId));
        response.setHeader("Authorization", "Bearer " + accessToken);
    }

    private void addRefreshTokenToCookidAndRedis(Long userId, HttpServletResponse response) {
        String refreshToken = tokenGenerator.generateToken(TokenType.REFRESH, String.valueOf(userId));

        cookieUtil.setCookie("refreshToken", refreshToken, response);
        redisUtil.setData(String.valueOf(userId), refreshToken);
    }
}

package com.example.jejugudgo.global.jwt.service;

import com.example.jejugudgo.global.jwt.token.TokenGenerator;
import com.example.jejugudgo.global.jwt.token.TokenType;
import com.example.jejugudgo.global.jwt.token.TokenUtil;
import com.example.jejugudgo.global.redis.RedisUtil;
import com.example.jejugudgo.global.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JWTRefreshService {
    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;
    private final TokenUtil tokenUtil;
    private final TokenGenerator tokenGenerator;

    public void refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookieFromRequest = cookieUtil.getCookie(request, "refreshToken");
        String refreshToken = cookieFromRequest.getValue();
        Long userId = tokenUtil.getUserIdFromToken(refreshToken);

        log.info("======================================================");
        log.info("refreshToken: " + refreshToken);
        log.info("userId: " + userId);
        log.info("======================================================");

        String redisValue = redisUtil.getData(String.valueOf(userId));
        if (refreshToken.equals(redisValue)) {
            String newAccessToken = tokenGenerator.generateToken(TokenType.ACCESS, String.valueOf(userId));
            String newRefreshToken = tokenGenerator.generateToken(TokenType.REFRESH, String.valueOf(userId));

            response.setHeader("Authorization", "Bearer " + newAccessToken);
            cookieUtil.setCookie("refreshToken", newRefreshToken, response);
            redisUtil.setData(String.valueOf(userId), newRefreshToken);
        }
    }
}
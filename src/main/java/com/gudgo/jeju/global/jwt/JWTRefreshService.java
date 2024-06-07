package com.gudgo.jeju.global.jwt;

import com.gudgo.jeju.global.jwt.token.SubjectExtractor;
import com.gudgo.jeju.global.jwt.token.TokenGenerator;
import com.gudgo.jeju.global.jwt.token.TokenType;
import com.gudgo.jeju.global.util.CookieUtil;
import com.gudgo.jeju.global.util.RedisUtil;
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
    private final SubjectExtractor subjectExtractor;
    private final TokenGenerator tokenGenerator;

    public void refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookieFromRequest = cookieUtil.getCookie(request, "refreshToken");
        String refreshToken = cookieFromRequest.getValue();
        String userId = subjectExtractor.getUserIdFromToken(refreshToken).toString();

        log.info("======================================================");
        log.info("refreshToken: " + refreshToken);
        log.info("userId: " + userId);
        log.info("======================================================");

        String redisValue = redisUtil.getData(userId);
        if (refreshToken.equals(redisValue)) {
            String newAccessToken = tokenGenerator.generateToken(TokenType.ACCESS, userId);
            String newRefreshToken = tokenGenerator.generateToken(TokenType.REFRESH, userId);

            response.setHeader("Authorization", "Bearer " + newAccessToken);
            cookieUtil.setCookie("refreshToken", newRefreshToken, response);
            redisUtil.setData(userId, newRefreshToken);
        }
    }
}
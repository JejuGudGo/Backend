package com.gudgo.jeju.global.auth.oauth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.auth.oauth.entity.CustomOAuth2User;
import com.gudgo.jeju.global.jwt.token.TokenGenerator;
import com.gudgo.jeju.global.jwt.token.TokenType;
import com.gudgo.jeju.global.util.CookieUtil;
import com.gudgo.jeju.global.util.RedisUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private String PRE_FRONT_REDIRECT_URL = "http://localhost:5173";
    private final TokenGenerator tokenGenerator;
    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String provider = customOAuth2User.getUser().getProvider();

        if (response.isCommitted()) {
            log.info("============================================================================");
            log.info("Social login response has been successfully sent.");
            log.info("============================================================================");
        }

        log.info("============================================================================");
        log.info("Social login successful. Social type is {}", provider);
        log.info("============================================================================");

        String accessToken = tokenGenerator.generateToken(TokenType.ACCESS, String.valueOf(customOAuth2User.getUser().getId()));
        String refreshToken = tokenGenerator.generateToken(TokenType.REFRESH, String.valueOf(customOAuth2User.getUser().getId()));

        response.setHeader("Authorization", "Bearer " + accessToken);
        cookieUtil.setCookie("refreshToken", refreshToken, response);
        redisUtil.setData(String.valueOf(customOAuth2User.getUser().getId()), refreshToken);

        String frontendRedirectUrl = setRedirectUrl(customOAuth2User);

        response.sendRedirect(frontendRedirectUrl);
    }

    private String setRedirectUrl (CustomOAuth2User customOAuth2User) {
        String encodedUserId = URLEncoder.encode(String.valueOf(customOAuth2User.getUser().getId()), StandardCharsets.UTF_8);
        String encodedNickname = URLEncoder.encode(String.valueOf(customOAuth2User.getUser().getNickname()), StandardCharsets.UTF_8);
        String encodedNumberTag = URLEncoder.encode(String.valueOf(customOAuth2User.getUser().getNumberTag()), StandardCharsets.UTF_8);

        String frontendRedirectURL = String.format(
                "%s/oauth/callback?userId=%s&nickname=%s&numberTag=%s",
                PRE_FRONT_REDIRECT_URL, encodedUserId, encodedNickname, encodedNumberTag
        );

        return frontendRedirectURL;
    }
}

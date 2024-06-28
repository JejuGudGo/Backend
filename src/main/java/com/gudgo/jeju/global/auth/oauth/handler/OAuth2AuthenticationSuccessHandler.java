package com.gudgo.jeju.global.auth.oauth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gudgo.jeju.domain.user.dto.UserInfoResponseDto;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.auth.oauth.entity.CustomOAuth2User;
import com.gudgo.jeju.global.jwt.token.TokenGenerator;
import com.gudgo.jeju.global.jwt.token.TokenType;
import com.gudgo.jeju.global.util.CookieUtil;
import com.gudgo.jeju.global.util.RedisUtil;
import jakarta.persistence.EntityNotFoundException;
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
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String provider = customOAuth2User.getUser().getProvider();

        User user = userRepository.findById(customOAuth2User.getUser().getId())
                .orElseThrow(EntityNotFoundException::new);

        UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getName(),
                user.getNumberTag(),
                user.getProfile().getProfileImageUrl(),
                user.getRole()
        );

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

        String frontendRedirectUrl = setRedirectUrl(accessToken, userInfoResponseDto);

        response.sendRedirect(frontendRedirectUrl);
    }

    private String setRedirectUrl (String accessToken, UserInfoResponseDto userInfoResponseDto) {
        String encodedUserId = URLEncoder.encode(String.valueOf(userInfoResponseDto.id()), StandardCharsets.UTF_8);
        String encodedEmail = URLEncoder.encode(String.valueOf(userInfoResponseDto.email()), StandardCharsets.UTF_8);
        String encodedNickname = URLEncoder.encode(String.valueOf(userInfoResponseDto.nickname()), StandardCharsets.UTF_8);
        String encodedName = URLEncoder.encode(String.valueOf(userInfoResponseDto.name()), StandardCharsets.UTF_8);
        String encodedNumberTag = URLEncoder.encode(String.valueOf(userInfoResponseDto.numberTag()), StandardCharsets.UTF_8);
        String encodedProfieImageUrl = URLEncoder.encode(String.valueOf(userInfoResponseDto.profileImageUrl()), StandardCharsets.UTF_8);
        String encodedUserRole = URLEncoder.encode(String.valueOf(userInfoResponseDto.userRole()), StandardCharsets.UTF_8);

        String frontendRedirectURL = String.format(
                "%s/oauth/callback?token=%s&userId=%s&email=%s&nickname=%s&name=%s&numberTag=%s&profileImageUrl=%s&userRole=%s",
                PRE_FRONT_REDIRECT_URL, accessToken, encodedUserId, encodedEmail, encodedNickname, encodedName, encodedNumberTag, encodedProfieImageUrl, encodedUserRole
        );

        return frontendRedirectURL;
    }
}

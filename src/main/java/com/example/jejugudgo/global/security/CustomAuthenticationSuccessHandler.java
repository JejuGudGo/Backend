package com.example.jejugudgo.global.security;

import com.example.jejugudgo.domain.user.athentication.signIn.dto.response.SignInResponse;
import com.example.jejugudgo.domain.user.common.entity.User;
import com.example.jejugudgo.domain.user.common.entity.UserProfile;
import com.example.jejugudgo.domain.user.common.repository.UserRepository;
import com.example.jejugudgo.global.exception.dto.CommonApiResponse;
import com.example.jejugudgo.global.exception.util.ApiResponseUtil;
import com.example.jejugudgo.global.jwt.token.TokenGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final TokenGenerator tokenGenerator;
    private final ApiResponseUtil apiResponseUtil;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Authentication authentication) throws IOException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.user();
        CommonApiResponse data = setSuccessResponse(httpResponse, user.getId());

        httpResponse.setContentType("application/json");
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setStatus(HttpServletResponse.SC_OK);
        printLog(user);

        new ObjectMapper().writeValue(httpResponse.getWriter(), data);
    }

    private CommonApiResponse setSuccessResponse(HttpServletResponse httpResponse, Long userId) {
        User user = userRepository.findById(userId)
                .orElse(null);

        String accessToken = tokenGenerator.generateToken(String.valueOf(user.getId()));
        String profileImageUrl = user.getUserProfile().getProfileImageUrl();

        SignInResponse response = new SignInResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getNickname(),
                profileImageUrl,
                accessToken
        );

        httpResponse.setHeader("Authorization", accessToken);

        return apiResponseUtil.success(response);
    }

    private void printLog(User user) {
        log.info("===========================================================================");
        log.info("Authentication successful!!");
        log.info("email: {}", user.getEmail());
        log.info("provider: {}", user.getProvider());
        log.info("===========================================================================");
    }
}


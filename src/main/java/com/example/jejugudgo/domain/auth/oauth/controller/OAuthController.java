package com.example.jejugudgo.domain.auth.oauth.controller;

import com.example.jejugudgo.domain.auth.oauth.repository.OAuthRequest;
import com.example.jejugudgo.domain.auth.oauth.service.OAuthService;
import com.example.jejugudgo.domain.user.dto.response.UserInfoResponse;
import com.example.jejugudgo.global.exception.dto.response.CommonApiResponse;
import com.example.jejugudgo.global.util.ApiResponseUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
public class OAuthController {
    private final ApiResponseUtil apiResponseUtil;
    private final OAuthService oAuthService;

    @PostMapping("/google")
    public ResponseEntity<CommonApiResponse> googleLogin(OAuthRequest request, HttpServletResponse response) {
        UserInfoResponse userInfoResponse = oAuthService.oauthLogin("google", request, response);
        return ResponseEntity.ok(apiResponseUtil.success(userInfoResponse));
    }

    @PostMapping("/kakao")
    public ResponseEntity<CommonApiResponse> kakaoLogin(OAuthRequest request, HttpServletResponse response) {
        UserInfoResponse userInfoResponse = oAuthService.oauthLogin("kakao", request, response);
        return ResponseEntity.ok(apiResponseUtil.success(userInfoResponse));
    }

    @PostMapping("/apple")
    public ResponseEntity<CommonApiResponse> appleLogin(OAuthRequest request, HttpServletResponse response) {
        UserInfoResponse userInfoResponse = oAuthService.oauthLogin("apple", request, response);
        return ResponseEntity.ok(apiResponseUtil.success(userInfoResponse));
    }
}

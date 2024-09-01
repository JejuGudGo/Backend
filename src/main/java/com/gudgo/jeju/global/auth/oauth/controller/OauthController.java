package com.gudgo.jeju.global.auth.oauth.controller;

import com.gudgo.jeju.global.auth.basic.dto.response.SignupResponse;
import com.gudgo.jeju.global.auth.oauth.dto.OAuth2LoginRequset;
import com.gudgo.jeju.global.auth.oauth.dto.OAuth2SignupRequest;
import com.gudgo.jeju.global.auth.oauth.service.OAuth2LoginService;
import com.gudgo.jeju.global.auth.oauth.service.OAuth2SignupService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth")
@RestController
public class OauthController {
    private final OAuth2SignupService oAuth2SignupService;
    private final OAuth2LoginService oAuth2LoginService;

    @PostMapping(value = "/oauth/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody OAuth2SignupRequest request) {
        oAuth2SignupService.androidSignup(request);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/oauth/login")
    public ResponseEntity<?> login(@Valid @RequestBody OAuth2LoginRequset request, HttpServletResponse httpServletResponse) {
        oAuth2LoginService.login(request, httpServletResponse);

        return ResponseEntity.ok().build();
    }
}

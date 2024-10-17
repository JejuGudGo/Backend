package com.example.jejugudgo.domain.auth.basic.controller;

import com.example.jejugudgo.domain.auth.basic.dto.request.LoginRequest;
import com.example.jejugudgo.domain.auth.basic.dto.request.SignupRequest;
import com.example.jejugudgo.domain.auth.basic.dto.response.SignupResponse;
import com.example.jejugudgo.domain.auth.basic.service.BasicAuthService;
import com.example.jejugudgo.domain.auth.mail.dto.EmailRequest;
import com.example.jejugudgo.domain.user.dto.response.UserInfoResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.RequestContextFilter;

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final BasicAuthService basicAuthService;
    private final RequestContextFilter requestContextFilter;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request) {
        SignupResponse response = basicAuthService.signup(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/check/email")
    public ResponseEntity<Boolean> checkEmailDuplicate(@RequestBody EmailRequest request) {
        boolean isDuplicate = basicAuthService.checkEmailDuplicate(request);
        return ResponseEntity.ok(isDuplicate);
    }


    @PostMapping("/login")
    public ResponseEntity<UserInfoResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");

        if (token == null) {
            return ResponseEntity.badRequest().body(null);
        }

        UserInfoResponse userInfoResponse = basicAuthService.loginAndGetUserInfo(loginRequest, token);

        return ResponseEntity.ok(userInfoResponse);
    }
}

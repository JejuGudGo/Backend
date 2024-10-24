package com.example.jejugudgo.domain.auth.basic.controller;

import com.example.jejugudgo.domain.auth.basic.dto.request.LoginRequest;
import com.example.jejugudgo.domain.auth.basic.dto.request.SignupRequest;
import com.example.jejugudgo.domain.auth.basic.dto.response.SignupResponse;
import com.example.jejugudgo.domain.auth.basic.service.BasicAuthService;
import com.example.jejugudgo.domain.auth.mail.dto.EmailRequest;
import com.example.jejugudgo.domain.user.dto.response.UserInfoResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final BasicAuthService basicAuthService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        basicAuthService.signup(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/check/email")
    public ResponseEntity<Boolean> checkEmailDuplicate(@RequestBody EmailRequest request) {
        boolean isDuplicate = basicAuthService.checkEmailDuplicate(request.email());
        return ResponseEntity.ok(isDuplicate);
    }


    @PostMapping("/login")
    public ResponseEntity<UserInfoResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        UserInfoResponse userInfoResponse = basicAuthService.loginAndGetUserInfo(loginRequest, response);

        return ResponseEntity.ok(userInfoResponse);
    }
}

package com.example.jejugudgo.domain.auth.basic.controller;

import com.example.jejugudgo.domain.auth.basic.dto.request.LoginRequest;
import com.example.jejugudgo.domain.auth.basic.dto.request.SignupRequest;
import com.example.jejugudgo.domain.auth.basic.dto.response.SignupResponse;
import com.example.jejugudgo.domain.auth.basic.service.BasicAuthService;
import com.example.jejugudgo.domain.auth.mail.dto.EmailRequest;
import com.example.jejugudgo.domain.user.dto.response.UserInfoResponse;
import com.example.jejugudgo.global.exception.entity.ApiResponse;
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
    public ResponseEntity<ApiResponse<Void>> signup(@RequestBody SignupRequest request) {
        basicAuthService.signup(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/check/email")
    public ResponseEntity<ApiResponse<Void>> checkEmailDuplicate(@RequestBody EmailRequest request) {
        basicAuthService.checkEmailDuplicate(request.email());
        return ResponseEntity.ok(ApiResponse.success(null));
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserInfoResponse>> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        UserInfoResponse userInfoResponse = basicAuthService.loginAndGetUserInfo(loginRequest, response);
        return ResponseEntity.ok(ApiResponse.success(userInfoResponse));
    }
}

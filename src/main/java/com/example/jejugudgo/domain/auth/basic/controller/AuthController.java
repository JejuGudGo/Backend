package com.example.jejugudgo.domain.auth.basic.controller;

import com.example.jejugudgo.domain.auth.basic.dto.request.LoginRequest;
import com.example.jejugudgo.domain.auth.basic.dto.request.SignupRequest;
import com.example.jejugudgo.domain.auth.basic.dto.response.SignupResponse;
import com.example.jejugudgo.domain.auth.basic.service.BasicAuthService;
import com.example.jejugudgo.domain.auth.mail.dto.EmailRequest;
import com.example.jejugudgo.domain.auth.validation.UserValidation;
import com.example.jejugudgo.domain.auth.basic.dto.response.UserInfoResponse;
import com.example.jejugudgo.global.exception.dto.CommonApiResponse;
import com.example.jejugudgo.global.exception.util.ApiResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final BasicAuthService basicAuthService;
    private final UserValidation userValidation;
    private final ApiResponseUtil apiResponseUtil;

    @PostMapping(value = "/api/v1/auth/signup")
    public ResponseEntity<CommonApiResponse> signup(@RequestBody SignupRequest request) {
        SignupResponse response = basicAuthService.signup(request);
        return ResponseEntity.ok(apiResponseUtil.success(response));
    }

    @PostMapping(value = "/api/v1/auth/check/email")
    public ResponseEntity<CommonApiResponse> checkEmailDuplicate(@RequestBody EmailRequest request) {
        userValidation.validateEmail(request.email());
        return ResponseEntity.ok(apiResponseUtil.success(null));
    }


    @PostMapping(value = "/api/v1/auth/login")
    public ResponseEntity<CommonApiResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        UserInfoResponse userInfoResponse = basicAuthService.loginAndGetUserInfo(loginRequest, response);
        return ResponseEntity.ok(apiResponseUtil.success(userInfoResponse));
    }

    @DeleteMapping(value = "/api/v1/users")
    public ResponseEntity<CommonApiResponse> deleteUser(HttpServletRequest request) {
        basicAuthService.deleteUser(request);
        return ResponseEntity.ok(apiResponseUtil.success(null));
    }
}

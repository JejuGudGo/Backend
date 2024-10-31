package com.example.jejugudgo.domain.auth.basic.controller;

import com.example.jejugudgo.domain.auth.basic.dto.request.LoginRequest;
import com.example.jejugudgo.domain.auth.basic.dto.request.SignupRequest;
import com.example.jejugudgo.domain.auth.basic.dto.response.SignupResponse;
import com.example.jejugudgo.domain.auth.basic.service.BasicAuthService;
import com.example.jejugudgo.domain.auth.mail.dto.EmailRequest;
import com.example.jejugudgo.domain.user.dto.response.UserInfoResponse;
import com.example.jejugudgo.global.exception.dto.response.CommonApiResponse;
import com.example.jejugudgo.global.util.ApiResponseUtil;
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
    private final ApiResponseUtil apiResponseUtil;

    @PostMapping("/signup")
    public ResponseEntity<CommonApiResponse> signup(@RequestBody SignupRequest request) {
        SignupResponse response = basicAuthService.signup(request);
        return ResponseEntity.ok(apiResponseUtil.success(response));
    }

    @PostMapping("/check/email")
    public ResponseEntity<CommonApiResponse> checkEmailDuplicate(@RequestBody EmailRequest request) {
        basicAuthService.checkEmailDuplicate(request.email());
        return ResponseEntity.ok(apiResponseUtil.success(null));
    }


    @PostMapping("/login")
    public ResponseEntity<CommonApiResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        UserInfoResponse userInfoResponse = basicAuthService.loginAndGetUserInfo(loginRequest, response);
        return ResponseEntity.ok(apiResponseUtil.success(userInfoResponse));
    }

}

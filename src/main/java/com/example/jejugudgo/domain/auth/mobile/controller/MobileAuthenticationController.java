package com.example.jejugudgo.domain.auth.mobile.controller;

import com.example.jejugudgo.domain.auth.mobile.dto.request.MobilAuthCodeRequest;
import com.example.jejugudgo.domain.auth.mobile.dto.request.MobileAuthenticationRequest;
import com.example.jejugudgo.domain.auth.mobile.service.MobileAuthenticationService;
import com.example.jejugudgo.global.exception.entity.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth/sms")
@RestController
public class MobileAuthenticationController {
    private final MobileAuthenticationService mobileAuthenticationService;

    @PostMapping(value = "/send")
    public ResponseEntity<ApiResponse<Void>> sendAuthenticationCode(@RequestBody MobilAuthCodeRequest request) {
        mobileAuthenticationService.sendAuthCode(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping(value = "/check")
    public ResponseEntity<ApiResponse<Void>> checkAuthenticationCode(@RequestBody MobileAuthenticationRequest request) {
        mobileAuthenticationService.checkAuthCode(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

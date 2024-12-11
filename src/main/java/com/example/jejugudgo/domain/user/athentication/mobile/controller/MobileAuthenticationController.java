package com.example.jejugudgo.domain.user.athentication.mobile.controller;

import com.example.jejugudgo.domain.user.athentication.mobile.dto.request.MobilAuthCodeRequest;
import com.example.jejugudgo.domain.user.athentication.mobile.dto.request.MobileAuthenticationRequest;
import com.example.jejugudgo.domain.user.athentication.mobile.service.MobileAuthenticationService;
import com.example.jejugudgo.global.exception.dto.CommonApiResponse;
import com.example.jejugudgo.global.exception.util.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth/sms")
@RestController
public class MobileAuthenticationController {
    private final MobileAuthenticationService mobileAuthenticationService;
    private final ApiResponseUtil apiResponseUtil;

    @PostMapping(value = "/send")
    public ResponseEntity<CommonApiResponse> sendAuthenticationCode(@RequestBody MobilAuthCodeRequest request) {
        mobileAuthenticationService.sendAuthenticationCode(request);
        return ResponseEntity.ok(apiResponseUtil.success(null));
    }

    @PostMapping(value = "/check")
    public ResponseEntity<CommonApiResponse> checkAuthenticationCode(@RequestBody MobileAuthenticationRequest request) {
        mobileAuthenticationService.validationAuthenticationCode(request);
        return ResponseEntity.ok(apiResponseUtil.success(null));
    }
}

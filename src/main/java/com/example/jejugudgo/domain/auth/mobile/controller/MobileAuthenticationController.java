package com.example.jejugudgo.domain.auth.mobile.controller;

import com.example.jejugudgo.domain.auth.mobile.dto.request.MobilAuthCodeRequest;
import com.example.jejugudgo.domain.auth.mobile.dto.request.MobileAuthenticationRequest;
import com.example.jejugudgo.domain.auth.mobile.service.MobileAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth/sms")
@RestController
public class MobileAuthenticationController {
    private final MobileAuthenticationService mobileAuthenticationService;

    @PostMapping(value = "/send")
    public ResponseEntity<?> sendAuthenticationCode(@RequestBody MobilAuthCodeRequest request) throws Exception {
        mobileAuthenticationService.sendAuthCode(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/check")
    public ResponseEntity<?> sendAuthenticationCode(@RequestBody MobileAuthenticationRequest request) throws Exception {
        mobileAuthenticationService.checkAuthCode(request);
        return ResponseEntity.ok().build();
    }
}

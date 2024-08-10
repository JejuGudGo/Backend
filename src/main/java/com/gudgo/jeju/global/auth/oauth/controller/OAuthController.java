package com.gudgo.jeju.global.auth.oauth.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.gudgo.jeju.global.auth.oauth.service.KakaoService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/oauth")
@RestController
public class OAuthController {
    private final KakaoService kakaoService;

    @PostMapping(value = "/kakao")
    public ResponseEntity<?> login(@RequestParam(value = "code") String code, HttpServletResponse response) throws JsonProcessingException {
        kakaoService.login(code, response);
        return ResponseEntity.ok().build();
    }
}

package com.example.jejugudgo.global.jwt.controller;

import com.example.jejugudgo.global.jwt.service.JWTRefreshService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/auth/token")
@RequiredArgsConstructor
public class JWTController {
    private final JWTRefreshService jwtRefreshService;

    @PostMapping(value = "/refresh")
    public void refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        jwtRefreshService.refreshAccessToken(request, response);
    }
}

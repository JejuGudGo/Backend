package com.gudgo.jeju.domain.badge.controller;

import com.gudgo.jeju.domain.badge.dto.response.BadgeResponseDto;
import com.gudgo.jeju.domain.badge.service.BadgeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/badge")
@RequiredArgsConstructor
@Slf4j
@RestController
public class BadgeController {

    private final BadgeService badgeService;

    // user 뱃지 조회
    @GetMapping(value = "")
    public ResponseEntity<List<BadgeResponseDto>> get(HttpServletRequest request) {
        return ResponseEntity.ok(badgeService.get(request));
    }


}

package com.example.jejugudgo.global.config.controller;

import com.example.jejugudgo.global.config.dto.response.VersionResponse;
import com.example.jejugudgo.global.exception.dto.response.CommonApiResponse;
import com.example.jejugudgo.global.util.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/version")
@RequiredArgsConstructor
public class VersionController {
    private final ApiResponseUtil apiResponseUtil;

    @GetMapping("")
    public ResponseEntity<CommonApiResponse> getVersion() {
        VersionResponse response = new VersionResponse(
                "v0.0.0"
        );
        return ResponseEntity.ok(apiResponseUtil.success(response));
    }
}

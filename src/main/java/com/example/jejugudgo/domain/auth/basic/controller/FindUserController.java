package com.example.jejugudgo.domain.auth.basic.controller;

import com.example.jejugudgo.domain.user.dto.request.FindEmailRequest;
import com.example.jejugudgo.domain.user.dto.request.PasswordUpdateRequest;
import com.example.jejugudgo.domain.user.dto.response.FindEmailResponse;
import com.example.jejugudgo.domain.auth.basic.service.FindUserInfoService;
import com.example.jejugudgo.domain.user.service.UserInfoService;
import com.example.jejugudgo.global.exception.dto.response.CommonApiResponse;
import com.example.jejugudgo.global.exception.entity.ApiResponse;
import com.example.jejugudgo.global.util.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
public class FindUserController {
    private final FindUserInfoService findUserInfoService;
    private final UserInfoService userInfoService;
    private final ApiResponseUtil apiResponseUtil;

    @PostMapping("/find/email")
    public ResponseEntity<CommonApiResponse> findUserByEmail(
            @RequestBody FindEmailRequest request) {
        List<FindEmailResponse> responses = findUserInfoService.findUserByNameAndPhone(request);
        return ResponseEntity.ok(apiResponseUtil.success(responses, "results"));
    }

    @PatchMapping("/find/update")
    public ResponseEntity<CommonApiResponse> updatePassword(@RequestBody PasswordUpdateRequest request) {
        userInfoService.updatePassword(request);
        return ResponseEntity.ok(apiResponseUtil.success(null));
    }
}

package com.example.jejugudgo.domain.auth.basic.controller;

import com.example.jejugudgo.domain.user.dto.request.FindEmailRequest;
import com.example.jejugudgo.domain.user.dto.request.PasswordUpdateRequest;
import com.example.jejugudgo.domain.user.dto.response.FindEmailResponse;
import com.example.jejugudgo.domain.auth.basic.service.FindUserInfoService;
import com.example.jejugudgo.domain.user.service.UserInfoService;
import com.example.jejugudgo.global.exception.entity.ApiResponse;
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

    @PostMapping("/find/email")
    public ResponseEntity<ApiResponse<List<FindEmailResponse>>> findUserByEmail(
            @RequestBody FindEmailRequest request) {
        List<FindEmailResponse> responses = findUserInfoService.findUserByNameAndPhone(request);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @PatchMapping("/find/update")
    public ResponseEntity<ApiResponse<Void>> updatePassword(@RequestBody PasswordUpdateRequest request) {
        userInfoService.updatePassword(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

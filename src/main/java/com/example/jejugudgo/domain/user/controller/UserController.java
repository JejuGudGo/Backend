package com.example.jejugudgo.domain.user.controller;

import com.example.jejugudgo.domain.user.service.UserService;
import com.example.jejugudgo.global.exception.dto.response.CommonApiResponse;
import com.example.jejugudgo.global.exception.entity.ApiResponse;
import com.example.jejugudgo.global.util.ApiResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ApiResponseUtil apiResponseUtil;

    @DeleteMapping("")
    public ResponseEntity<CommonApiResponse> deleteUser(HttpServletRequest request) {
        userService.deleteUser(request);
        return ResponseEntity.ok(apiResponseUtil.success(null));
    }
}

package com.example.jejugudgo.domain.user.controller;

import com.example.jejugudgo.domain.user.service.DeleteUserService;
import com.example.jejugudgo.domain.user.service.UserInfoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
public class DeleteUserController {


    private final DeleteUserService deleteUserService;

    @DeleteMapping("")
    public ResponseEntity<?> deleteUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        Long authenticatedUserId = deleteUserService.getAuthenticatedUserIdFromToken(token);
        deleteUserService.deleteUser(authenticatedUserId);
        return ResponseEntity.ok().build();
    }
}

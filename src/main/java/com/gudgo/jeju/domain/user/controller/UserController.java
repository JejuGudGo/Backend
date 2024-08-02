package com.gudgo.jeju.domain.user.controller;


import com.gudgo.jeju.domain.user.dto.UserInfoResponseDto;
import com.gudgo.jeju.domain.user.dto.UserInfoUpdateRequestDto;
import com.gudgo.jeju.domain.user.service.UserInfoService;
import com.gudgo.jeju.global.jwt.token.SubjectExtractor;
import com.gudgo.jeju.global.jwt.token.TokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/api/v1/user")
@RequiredArgsConstructor
@Slf4j
@RestController
public class UserController {
    private final UserInfoService userInfoService;

    @PatchMapping(value = "/{id}")
    public ResponseEntity<UserInfoResponseDto> updateUserInfo(
            @PathVariable("id") Long userId,
            @RequestBody UserInfoUpdateRequestDto requestDto) {
        userInfoService.update(userId, requestDto);
        return ResponseEntity.ok().build();
    }

}

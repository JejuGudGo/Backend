package com.gudgo.jeju.global.auth.basic.controller;


import com.gudgo.jeju.global.auth.basic.dto.request.*;
import com.gudgo.jeju.global.auth.basic.dto.response.FindAuthResponseDto;
import com.gudgo.jeju.global.auth.basic.service.FindAuthService;
import com.gudgo.jeju.global.auth.basic.service.LoginService;
import com.gudgo.jeju.global.auth.basic.service.SignupService;
import com.gudgo.jeju.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth")
@RestController
public class AuthController {
    private final SignupService signupService;
    private final LoginService loginService;
    private final FindAuthService findAuthService;
    private final CookieUtil cookieUtil;

    /* 회원가입 */
    @PostMapping(value = "/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest) {
        signupService.signup(signupRequest);
        return ResponseEntity.ok().build();
    }


    /* 로그인 */
    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        loginService.login(loginRequest, response);
        return ResponseEntity.ok().build();
    }

    /* 로그아웃 */
    @PostMapping(value = "/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        response.setHeader("Authorization", "");
        cookieUtil.deleteCookie("refreshToken", response);
        return ResponseEntity.ok().build();
    }

    /* 이메일 인증번호 확인*/
    @PostMapping(value = "/authentication/check")
    public ResponseEntity<?> checkAuthenticationcode(@RequestBody AuthenticationRequest request) {
        findAuthService.validateAuthCode(request.email(), request.authCode());
        return ResponseEntity.ok().build();
    }

    /* ID 중복 확인 */
    @PostMapping(value = "/chaeck/id")
    public ResponseEntity<?> checkIdDuplicate(@RequestBody EmailRequestDto requestDto) {
        return signupService.isIdDuplicate(requestDto);
    }

    /* 아이디 찾기 */
    @PostMapping(value = "/find/id")
    public ResponseEntity<List<FindAuthResponseDto>> getId(@RequestBody FindAuthByPhoneRequestDto requestDto) {
        return findAuthService.getId(requestDto);
    }

//    /* 카카오 callback */
//    @GetMapping(value = "/kakao/login")
//    public void kakaoCallback(@RequestParam String code) {
//        System.out.println("code=" + code);
//    }
//
//    @PostMapping(value = "/kakao/login")
//    public ResponseEntity<?> kakaoLogin(@RequestParam(value="code", required = false) String code) throws Exception {
//        System.out.println("code=" + code);
//        String accessToken = kakaoOAuth2Service.getAccessToken(code);
//        return ResponseEntity.ok().build();
//    }

}

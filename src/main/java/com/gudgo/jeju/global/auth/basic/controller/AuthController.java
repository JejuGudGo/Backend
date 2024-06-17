package com.gudgo.jeju.global.auth.basic.controller;


import com.gudgo.jeju.global.auth.basic.dto.request.LoginRequest;
import com.gudgo.jeju.global.auth.basic.dto.request.SignupRequest;
import com.gudgo.jeju.global.auth.basic.service.LoginService;
import com.gudgo.jeju.global.auth.basic.service.SignupService;
import com.gudgo.jeju.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/auth")
@RestController
public class AuthController {
    private final SignupService signupService;
    private final LoginService loginService;
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

}

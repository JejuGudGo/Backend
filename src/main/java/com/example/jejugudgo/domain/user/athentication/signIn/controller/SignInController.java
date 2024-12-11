package com.example.jejugudgo.domain.user.athentication.signIn.controller;

import com.example.jejugudgo.domain.user.athentication.signIn.dto.request.OauthRequest;
import com.example.jejugudgo.domain.user.athentication.signIn.dto.request.SignInRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/signin")
@RequiredArgsConstructor
public class SignInController {

    @PostMapping(value = "")
    public void signIn(@RequestBody SignInRequest request) {
    }

    @PostMapping(value = "/kakao")
    public void kakaoSignIn(@RequestBody OauthRequest request) {
    }

    @PostMapping(value = "/google")
    public void googleSignIn(@RequestBody OauthRequest request) {
    }

    @PostMapping(value = "/apple")
    public void appleSignIn(@RequestBody OauthRequest request) {
    }
}

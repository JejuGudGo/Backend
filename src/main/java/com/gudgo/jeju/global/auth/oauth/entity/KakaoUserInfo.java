package com.gudgo.jeju.global.auth.oauth.entity;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class KakaoUserInfo implements OAuth2UserInfo{
    private final Map<String, Object> attributes;


    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("nickname").toString();
    }

    @Override
    public String getProfile() {
        return attributes.get("profile_image").toString();
    }

    @Override
    public String getPassword() {
        return null;
    }
}

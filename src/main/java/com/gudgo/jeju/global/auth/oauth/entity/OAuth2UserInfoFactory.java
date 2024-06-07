package com.gudgo.jeju.global.auth.oauth.entity;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuthUserInfo(String provider, Map<String, Object> attributes) {
        if (provider.equals("google")) {
            log.info("=============================================================");
            log.info("Google login Request sent");
            log.info("=============================================================");
            return new GoogleUserInfo(attributes);
        }
        return null;
    }
}

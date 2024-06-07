package com.gudgo.jeju.global.auth.oauth.service;

import com.gudgo.jeju.domain.user.entity.Role;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.auth.oauth.entity.OAuth2UserInfo;
import com.gudgo.jeju.global.util.image.entity.Category;
import com.gudgo.jeju.global.util.image.entity.Image;
import com.gudgo.jeju.global.util.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OAuth2SignupService {
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

//    public User signup(String provider, OAuth2UserInfo oAuth2UserInfo) {
//
//    }
}

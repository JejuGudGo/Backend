package com.gudgo.jeju.global.auth.oauth.service;

import com.gudgo.jeju.domain.profile.entity.Profile;
import com.gudgo.jeju.domain.profile.repository.ProfileRepository;
import com.gudgo.jeju.domain.user.entity.Role;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.auth.oauth.entity.OAuth2UserInfo;
import com.gudgo.jeju.global.util.RandomNumberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OAuth2SignupService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final RandomNumberUtil randomNumberUtil;

    public User signup(String provider, OAuth2UserInfo oAuth2UserInfo) {
        Profile profile = Profile.builder()
                .profileImageUrl(oAuth2UserInfo.getProfile())
                .build();

        profileRepository.save(profile);

        User user = User.builder()
                .profile(profile)
                .email(oAuth2UserInfo.getEmail())
                .password(oAuth2UserInfo.getPassword())
                .nickname(oAuth2UserInfo.getName())
                .numberTag(randomNumberUtil.set())
                .role(Role.USER)
                .provider(provider)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        return user;
    }
}

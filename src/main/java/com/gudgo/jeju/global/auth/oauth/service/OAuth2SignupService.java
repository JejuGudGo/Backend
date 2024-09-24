package com.gudgo.jeju.global.auth.oauth.service;

import com.gudgo.jeju.domain.badge.entity.BadgeCode;
import com.gudgo.jeju.domain.badge.event.BadgeEvent;
import com.gudgo.jeju.domain.profile.entity.Profile;
import com.gudgo.jeju.domain.profile.repository.ProfileRepository;
import com.gudgo.jeju.domain.todo.event.TodoEvent;
import com.gudgo.jeju.domain.user.entity.Role;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.auth.oauth.dto.OAuth2SignupRequest;
import com.gudgo.jeju.global.auth.oauth.entity.OAuth2UserInfo;
import com.gudgo.jeju.global.util.RandomNicknameUtil;
import com.gudgo.jeju.global.util.RandomNumberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OAuth2SignupService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final RandomNumberUtil randomNumberUtil;
    private final RandomNicknameUtil randomNicknameUtil;
    private final ApplicationEventPublisher eventPublisher;


    public User signup(String provider, OAuth2UserInfo oAuth2UserInfo) {
        Profile profile = Profile.builder()
                .profileImageUrl(oAuth2UserInfo.getProfile())
                .build();

        profileRepository.save(profile);

        User user = User.builder()
                .profile(profile)
                .email(oAuth2UserInfo.getEmail())
                .password(oAuth2UserInfo.getPassword())
                .nickname(randomNicknameUtil.set())
                .numberTag(randomNumberUtil.set())
                .role(Role.USER)
                .provider(provider)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        // 회원가입 시 뱃지 이벤트 발생
        eventPublisher.publishEvent(new BadgeEvent(user.getId(), BadgeCode.B01));

        // 회원가입 시 체크리스트 생성
        eventPublisher.publishEvent(new TodoEvent(user.getId()));

        return user;
    }

    public void androidSignup(OAuth2SignupRequest request) {
        Profile profile = Profile.builder()
                .profileImageUrl(request.profileImage())
                .build();

        profileRepository.save(profile);

        User user = User.builder()
                .profile(profile)
                .email(request.email())
                .password(null)
                .nickname(randomNicknameUtil.set())
                .numberTag(randomNumberUtil.set())
                .role(Role.USER)
                .provider(request.provider())
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        // 회원가입 시 뱃지 이벤트 발생
        eventPublisher.publishEvent(new BadgeEvent(user.getId(), BadgeCode.B01));

        // 회원가입 시 체크리스트 생성
        eventPublisher.publishEvent(new TodoEvent(user.getId()));

    }
}

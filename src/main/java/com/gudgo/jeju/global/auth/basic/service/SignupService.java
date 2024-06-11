package com.gudgo.jeju.global.auth.basic.service;


import com.gudgo.jeju.domain.user.dto.SignupRequestDto;
import com.gudgo.jeju.domain.user.entity.Role;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.auth.basic.dto.request.SignupRequest;
import com.gudgo.jeju.global.util.RandomNicknameUtil;
import com.gudgo.jeju.global.util.RandomNumberUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class SignupService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RandomNumberUtil randomNumberUtil;
    private final RandomNicknameUtil randomNicknameUtil;

    @Transactional
    public void signup(@Valid SignupRequest signupRequestDto) {
        // 이메일 중복 확인
        userRepository.findByEmailAndProvider(signupRequestDto.email(), signupRequestDto.provider())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("User with this email and provider already exists");
                });

        String nickname = signupRequestDto.nickname();

        // 사용자가 닉네임을 입력하지 않은 경우 : 랜덤 닉네임 생성
        if (nickname == null || nickname.isEmpty()) {
            nickname = randomNicknameUtil.set();
        } else {
            // 사용자가 입력한 닉네임에 대해 중복 확인
            userRepository.findByNickname(signupRequestDto.nickname())
                    .ifPresent(u-> {
                        throw new IllegalArgumentException("Nickname already exists");
                    });
        }


        User user = User.builder()
                .email(signupRequestDto.email())
                .password(passwordEncoder.encode(signupRequestDto.password()))
                .nickname(nickname)
                .numberTag(randomNumberUtil.set())
                .role(Role.USER)
                .provider("basic")
                .createdAt(LocalDateTime.now())
                .isDeleted(signupRequestDto.isDeleted())
                .build();

        userRepository.save(user);
    }
}

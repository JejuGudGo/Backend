package com.gudgo.jeju.global.auth.basic.service;


import com.gudgo.jeju.domain.user.entity.Role;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.auth.basic.dto.request.SignupRequest;
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

    @Transactional
    public void signup(@Valid SignupRequest signupRequestDto) {
        userRepository.findByEmailAndProvider(signupRequestDto.email(), signupRequestDto.provider())
                .ifPresent(u -> {
                    throw new IllegalArgumentException();
                });

        User user = User.builder()
                .email(signupRequestDto.email())
                .password(passwordEncoder.encode(signupRequestDto.password()))
                .nickname(signupRequestDto.nickname())
                .numberTag(randomNumberUtil.set())
                .role(Role.USER)
                .provider("basic")
                .createdAt(LocalDateTime.now())
                .isDeleted(signupRequestDto.isDeleted())
                .build();

        userRepository.save(user);
    }

}

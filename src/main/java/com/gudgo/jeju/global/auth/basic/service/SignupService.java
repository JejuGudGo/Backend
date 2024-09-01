package com.gudgo.jeju.global.auth.basic.service;


import com.gudgo.jeju.domain.profile.entity.Profile;
import com.gudgo.jeju.domain.profile.repository.ProfileRepository;
import com.gudgo.jeju.domain.user.entity.Role;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.auth.basic.dto.request.EmailRequestDto;
import com.gudgo.jeju.global.auth.basic.dto.request.SignupRequest;
import com.gudgo.jeju.global.auth.basic.dto.response.SignupResponse;
import com.gudgo.jeju.global.util.RandomNicknameUtil;
import com.gudgo.jeju.global.util.RandomNumberUtil;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final ProfileRepository profileRepository;

    @Transactional
    public SignupResponse signup(@Valid SignupRequest signupRequestDto) {
        // 이메일 중복 확인
        userRepository.findByEmailAndProvider(signupRequestDto.email(), "basic")
                .ifPresent(u -> {
                    throw new IllegalArgumentException("User with this email and provider already exists");
                });

        String nickname = (randomNicknameUtil.set());

        Profile profile = Profile.builder()
                .profileImageUrl("Default")
                .build();

        User user = User.builder()
                .profile(profile)
                .email(signupRequestDto.email())
                .password(passwordEncoder.encode(signupRequestDto.password()))
                .nickname(nickname)
                .numberTag(randomNumberUtil.set())
                .role(Role.USER)
                .provider("basic")
                .createdAt(LocalDateTime.now())
                .isDeleted(false)
                .name(signupRequestDto.name())
                .phoneNumber(signupRequestDto.phoneNumber())
                .build();

        userRepository.save(user);
        profileRepository.save(profile);

        SignupResponse response = new SignupResponse(nickname);
        return response;
    }

    public ResponseEntity<?> isIdDuplicate(EmailRequestDto requestDto) {
        boolean isDuplicate = userRepository.findByEmail(requestDto.email()).isPresent();
        if (isDuplicate) throw new EntityExistsException("INVALID_VALUE_04"); // 중복일 경우 400 BadRequest 반환 + ErrorCode : INVALID_VALUE_04 반환
        else return ResponseEntity.ok().build(); // 중복이 아닐 경우 200 OK 반환
    }
}
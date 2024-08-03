package com.gudgo.jeju.global.auth.basic.service;


import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.auth.basic.dto.request.FindAuthByPhoneRequestDto;
import com.gudgo.jeju.global.auth.basic.dto.response.FindAuthResponseDto;
import com.gudgo.jeju.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class FindAuthService {
    private final UserRepository userRepository;
    private final RedisUtil redisUtil;


    public ResponseEntity<List<FindAuthResponseDto>> getId(FindAuthByPhoneRequestDto requestDto) {
        List<User> users = userRepository.findByPhoneNumberAndName(requestDto.phoneNumber() , requestDto.name());

        users.removeIf(user -> !user.getProvider().equals("basic")); // 소셜로그인 이메일 방지

        List<FindAuthResponseDto> responses = users.stream()
                .map(user -> new FindAuthResponseDto(
                        user.getId(),
                        user.getEmail(),
                        user.getName(),
                        user.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);


    }

    public void validateAuthCode(String email, String authCode) {
        String authCodeFromRedis = redisUtil.getData(email);

        if(!authCode.equals(authCodeFromRedis)) {
            throw new IllegalArgumentException();
        }

    }
}

package com.example.jejugudgo.domain.user.service;

import com.example.jejugudgo.domain.user.entity.User;
import com.example.jejugudgo.domain.user.repository.UserRepository;
import com.example.jejugudgo.global.jwt.token.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteUserService {
    private final UserRepository userRepository;
    private final TokenUtil tokenUtil;

    // 사용자 삭제 로직
    public void deleteUser(Long userId) {
        // 1. 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        // 2. 사용자 삭제
        userRepository.delete(user);
    }

    // 토큰에서 인증된 사용자 ID 추출
    public Long getAuthenticatedUserIdFromToken(String token) {
        // Bearer 토큰 앞부분을 제거하고 실제 토큰 값만 사용
        token = token.replace("Bearer ", "");

        // 토큰 유효성 검증
        tokenUtil.validateAccessToken(token);

        // 토큰에서 사용자 ID 추출
        return tokenUtil.getUserIdFromToken(token);
    }
}

package com.example.jejugudgo.domain.user.service;

import com.example.jejugudgo.domain.user.entity.User;
import com.example.jejugudgo.domain.user.repository.UserRepository;
import com.example.jejugudgo.global.jwt.token.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TokenUtil tokenUtil;

    // 사용자 삭제 로직
    public void deleteUser(HttpServletRequest request) {
        // 1. 사용자 조회
        Long userId = tokenUtil.getUserIdFromHeader(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        // 2. 사용자 삭제
        user = user.updateUserStatus();
        userRepository.save(user);
    }
}

package com.example.jejugudgo.domain.user.service;

import com.example.jejugudgo.domain.user.entity.User;
import com.example.jejugudgo.domain.user.repository.UserRepository;
import com.example.jejugudgo.global.jwt.token.TokenUtil;
import com.example.jejugudgo.global.redis.RedisUtil;
import com.example.jejugudgo.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CookieUtil cookieUtil;
    private final TokenUtil tokenUtil;
    private final RedisUtil redisUtil;

    // 사용자 삭제 로직
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) {
        // 1. 사용자 조회
        Long userId = getAuthenticatedUserIdFromToken(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        // 2. 사용자 삭제
        user = user.updateUserStatus();
        userRepository.save(user);

        // 3. 쿠키, redis 삭제
        cookieUtil.deleteCookie("accessToken", response);
        cookieUtil.deleteCookie("refreshToken", response);
        redisUtil.deleteData(String.valueOf(userId));
    }

    // 토큰에서 인증된 사용자 ID 추출
    public Long getAuthenticatedUserIdFromToken(HttpServletRequest request) {
        // Bearer 토큰 앞부분을 제거하고 실제 토큰 값만 사용
        String accessToken = cookieUtil.getCookie(request, "accessToken").getValue();
        Long userId = tokenUtil.getUserIdFromToken(accessToken);

        return userId;
    }
}

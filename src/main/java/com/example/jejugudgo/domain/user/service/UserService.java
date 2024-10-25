package com.example.jejugudgo.domain.user.service;

import com.example.jejugudgo.domain.user.entity.User;
import com.example.jejugudgo.domain.user.repository.UserRepository;
import com.example.jejugudgo.global.exception.CustomException;
import com.example.jejugudgo.global.exception.entity.RetCode;
import com.example.jejugudgo.global.jwt.token.TokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
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
        try {
            // 1. 사용자 조회
            Long userId = tokenUtil.getUserIdFromHeader(request);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(RetCode.RET_CODE99));

            // 2. 사용자 삭제
            user = user.updateUserStatus();
            userRepository.save(user);

        } catch (ExpiredJwtException e) {
            throw new CustomException(RetCode.RET_CODE98);  // 토큰 만료
        } catch (Exception e) {
            throw new CustomException(RetCode.RET_CODE99);  // 기타 서버 오류
        }
    }
}

package com.example.jejugudgo.domain.user.service;

import com.example.jejugudgo.domain.user.dto.request.PasswordUpdateRequest;
import com.example.jejugudgo.domain.user.entity.Provider;
import com.example.jejugudgo.domain.user.entity.User;
import com.example.jejugudgo.domain.user.repository.UserRepository;
import com.example.jejugudgo.global.exception.CustomException;
import com.example.jejugudgo.global.exception.entity.RetCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserInfoService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&#]{8,20}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    @Transactional
    public void updatePassword(PasswordUpdateRequest request) {
        // 비밀번호 형식 검증
        if (!isValidPassword(request.password())) {
            throw new CustomException(RetCode.RET_CODE06);  // 비밀번호 형식이 올바르지 않습니다
        }

        User user = userRepository.findByEmailAndProvider(request.email(), Provider.BASIC)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE08));  // 존재하지 않는 이메일

        user.updatePassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);
    }

    private boolean isValidPassword(String password) {
        if (password == null) return false;
        return pattern.matcher(password).matches();
    }
}
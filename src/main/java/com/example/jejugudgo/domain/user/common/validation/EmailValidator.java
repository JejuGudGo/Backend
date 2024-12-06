package com.example.jejugudgo.domain.user.common.validation;


import com.example.jejugudgo.domain.user.common.enums.Provider;
import com.example.jejugudgo.domain.user.common.repository.UserRepository;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailValidator {
    private final UserRepository userRepository;

    public void isEmailDuplicated(String email) {
        userRepository.findByEmailAndProvider(email, Provider.BASIC)
                .ifPresent(user -> {
                    // TODO: 회원탈퇴 여부 따라 분기처리
                    throw new CustomException(RetCode.RET_CODE07);
                });
    }
}

package com.example.jejugudgo.domain.auth.validation;

import com.example.jejugudgo.domain.user.user.entity.Provider;
import com.example.jejugudgo.domain.user.user.repository.UserRepository;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.exception.enums.RetCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidation {
    private final UserRepository userRepository;

    public void validateEmail(String email) {
        if (isDuplicatedEmail(email)) throw new CustomException(RetCode.RET_CODE07);
    }

    private boolean isDuplicatedEmail(String email) {
        return userRepository.findByEmailAndProvider(email, Provider.BASIC).isPresent();
    }
}

package com.example.jejugudgo.domain.auth.basic.service;

import com.example.jejugudgo.domain.auth.basic.dto.request.PasswordUpdateRequest;
import com.example.jejugudgo.domain.auth.validation.PasswordValidation;
import com.example.jejugudgo.domain.auth.validation.PhoneValidation;
import com.example.jejugudgo.domain.auth.basic.dto.request.FindEmailRequest;
import com.example.jejugudgo.domain.auth.basic.dto.response.FindEmailResponse;
import com.example.jejugudgo.domain.user.user.entity.Provider;
import com.example.jejugudgo.domain.user.user.entity.User;
import com.example.jejugudgo.domain.user.user.repository.UserRepository;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.exception.enums.RetCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FindUserInfoService {
    private final UserRepository userRepository;
    private final PhoneValidation phoneValidation;
    private final PasswordValidation passwordValidation;

    public List<FindEmailResponse> findUserByNameAndPhone(FindEmailRequest request) {
        String phoneNumber = request.phoneNumber();

        // 휴대폰 번호 형식 검증
        phoneValidation.validatePhoneNumber(phoneNumber);

        List<User> users = userRepository.findByPhoneNumberAndName(phoneNumber, request.name());

        if (users.isEmpty()) {
            throw new CustomException(RetCode.RET_CODE05);
        }

        List<FindEmailResponse> responses = new ArrayList<>();
        for (User user : users) {
            FindEmailResponse response = new FindEmailResponse(
                    user.getEmail(),
                    user.getCreatedAt()
            );
            responses.add(response);
        }

        return responses;
    }

    @Transactional
    public void updatePassword(PasswordUpdateRequest request) {
        // 비밀번호 형식 검증
        String encodedPassword = passwordValidation.validatePassword(request.password());

        User user = userRepository.findByEmailAndProvider(request.email(), Provider.BASIC)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE08));  // 존재하지 않는 이메일

        user.updatePassword(encodedPassword);
        userRepository.save(user);
    }
}
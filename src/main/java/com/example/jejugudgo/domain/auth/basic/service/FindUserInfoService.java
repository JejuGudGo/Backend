package com.example.jejugudgo.domain.auth.basic.service;

import com.example.jejugudgo.domain.user.dto.request.FindEmailRequest;
import com.example.jejugudgo.domain.user.dto.response.FindEmailResponse;
import com.example.jejugudgo.domain.user.entity.User;
import com.example.jejugudgo.domain.user.repository.UserRepository;
import com.example.jejugudgo.global.exception.CustomException;
import com.example.jejugudgo.global.exception.entity.RetCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FindUserInfoService {
    private final UserRepository userRepository;

    public List<FindEmailResponse> findUserByNameAndPhone(FindEmailRequest request) {
        String phoneNumber = request.phoneNumber();

        // 휴대폰 번호 형식 검증
        if (!isValidPhoneNumber(phoneNumber)) {
            throw new CustomException(RetCode.RET_CODE03);
        }

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

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("^010[0-9]{8}$");  // 010 + 8자리 숫자
    }
}
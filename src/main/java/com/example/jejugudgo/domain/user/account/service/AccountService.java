package com.example.jejugudgo.domain.user.account.service;

import com.example.jejugudgo.domain.user.account.dto.request.FindEmailRequest;
import com.example.jejugudgo.domain.user.account.dto.request.PasswordUpdateRequest;
import com.example.jejugudgo.domain.user.account.dto.response.FindEmailResponse;
import com.example.jejugudgo.domain.user.common.entity.User;
import com.example.jejugudgo.domain.user.common.repository.UserRepository;
import com.example.jejugudgo.domain.user.common.validation.ValidationManager;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.jwt.token.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final ValidationManager validationManager;
    private final UserProfileService userProfileService;
    private final TokenUtil tokenUtil;
    private final UserRepository userRepository;

    public List<FindEmailResponse> findEmail(FindEmailRequest request) {
        String phoneNumber = request.phoneNumber();
        List<User> users = userRepository.findByPhoneNumberAndName(phoneNumber, request.name());

        List<FindEmailResponse> responses = users.stream()
                .map(user -> new FindEmailResponse(user.getEmail(), user.getCreatedAt()))
                .collect(Collectors.toList());

        return responses;
    }

    // TODO: 회원탈퇴 이후 절차 문의
    public void cancelAccount(HttpServletRequest request) {
        String userId = tokenUtil.getAccessTokenFromHeader(request);
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE07));

        user = user.updateDeletedAt();
        userRepository.save(user);
    }
}

package com.example.jejugudgo.domain.auth.basic.service;

import com.example.jejugudgo.domain.auth.basic.dto.request.LoginRequest;
import com.example.jejugudgo.domain.auth.basic.dto.request.SignupRequest;
import com.example.jejugudgo.domain.auth.basic.dto.response.SignupResponse;
import com.example.jejugudgo.domain.auth.mail.dto.EmailRequest;
import com.example.jejugudgo.domain.auth.terms.dto.request.TermsAgreementRequest;
import com.example.jejugudgo.domain.auth.terms.entity.Terms;
import com.example.jejugudgo.domain.auth.terms.repository.TermsRepository;
import com.example.jejugudgo.domain.user.dto.response.UserInfoResponse;
import com.example.jejugudgo.domain.user.entity.Provider;
import com.example.jejugudgo.domain.user.entity.Role;
import com.example.jejugudgo.domain.user.entity.User;
import com.example.jejugudgo.domain.user.entity.UserTerms;
import com.example.jejugudgo.domain.user.repository.UserRepository;
import com.example.jejugudgo.domain.user.repository.UserTermsRepository;
import com.example.jejugudgo.global.jwt.token.TokenUtil;
import com.example.jejugudgo.global.util.RandomNicknameUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicAuthService {

    private final UserRepository userRepository;
    private final TermsRepository termsRepository;
    private final UserTermsRepository userTermsRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenUtil tokenUtil;
    private final RandomNicknameUtil randomNicknameUtil;

    public SignupResponse signup(SignupRequest request) {

        // random 닉네임 생성
        String nickname = randomNicknameUtil.set();

        // 1. 회원 정보 저장
        User user = User.builder()
                .email(request.email())
                .password(request.password())
                .name(request.name())
                .nickname(nickname)
                .phoneNumber(request.phoneNumber())
                .createdAt(LocalDateTime.now())
                .role(Role.USER)
                .provider(Provider.BASIC)
                .build();

        userRepository.save(user);

        // 2. 약관 동의 처리
        List<TermsAgreementRequest> termsAgreementRequests = request.terms();

        for (TermsAgreementRequest agreementRequest : termsAgreementRequests) {
            Long termsId = agreementRequest.termsId();
            boolean isAgreed = agreementRequest.isAgreed();

            Terms terms = termsRepository.findById(termsId)
                    .orElseThrow(EntityNotFoundException::new);

            UserTerms userTerms = UserTerms.builder()
                    .user(user)
                    .termsId(terms.getId())
                    .isAgreed(isAgreed)
                    .build();

            userTermsRepository.save(userTerms);
        }

        // 3. response 생성
        return new SignupResponse(
                user.getId(),
                user.getName(),
                user.getNickname()
        );
    }

    public boolean checkEmailDuplicate(EmailRequest request) {
        return userRepository.findByEmail(request.email()).isPresent();
    }

    public UserInfoResponse loginAndGetUserInfo(LoginRequest request, String token) {
        // 1. 유저 정보 조회
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(EntityNotFoundException::new);

        // 2. 비밀번호 검증
        if (!bCryptPasswordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException();
        }

        // 3. 토큰 검증
        tokenUtil.validateAccessToken(token);

        return new UserInfoResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getNickname(),
                user.getCreatedAt(),
                user.getPhoneNumber(),
                user.getRole()
        );
    }
}

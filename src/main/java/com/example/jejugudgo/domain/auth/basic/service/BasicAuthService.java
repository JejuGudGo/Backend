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

    // 회원가입 메서드
    public SignupResponse signup(SignupRequest request) {
        // 1. 비밀번호 유효성 검사
        validatePassword(request.password());

        // 2. 사용자 정보 생성 및 저장
        User user = createUser(request);

        // 3. 약관 동의 처리
        handleTermsAgreements(request.terms(), user);

        // 4. 응답 생성
        return createSignupResponse(user);
    }

    // 비밀번호 유효성 검사 메서드
    private void validatePassword(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$";
        if (!password.matches(passwordPattern)) {
            throw new IllegalArgumentException();
        }
    }

    // 사용자 생성 메서드
    private User createUser(SignupRequest request) {

        // 랜덤 닉네임 생성
        String nickname = randomNicknameUtil.set();

        User user = User.builder()
                .email(request.email())
                .password(bCryptPasswordEncoder.encode(request.password()))  // 비밀번호 암호화
                .name(request.name())
                .nickname(nickname)
                .phoneNumber(request.phoneNumber())
                .createdAt(LocalDateTime.now())
                .role(Role.USER)
                .provider(Provider.BASIC)
                .build();

        return userRepository.save(user);
    }

    // 약관 동의 처리 메서드
    private void handleTermsAgreements(List<TermsAgreementRequest> termsAgreementRequests, User user) {
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
    }

    // 응답 생성 메서드
    private SignupResponse createSignupResponse(User user) {
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

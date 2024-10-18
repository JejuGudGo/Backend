package com.example.jejugudgo.domain.auth.basic.service;

import com.example.jejugudgo.domain.auth.basic.dto.request.LoginRequest;
import com.example.jejugudgo.domain.auth.basic.dto.request.SignupRequest;
import com.example.jejugudgo.domain.auth.basic.dto.response.SignupResponse;
import com.example.jejugudgo.domain.auth.mail.dto.EmailRequest;
import com.example.jejugudgo.domain.auth.terms.dto.request.TermsAgreementRequest;
import com.example.jejugudgo.domain.auth.terms.entity.Terms;
import com.example.jejugudgo.domain.auth.terms.repository.TermsRepository;
import com.example.jejugudgo.domain.profile.entity.UserProfile;
import com.example.jejugudgo.domain.profile.service.UserProfileService;
import com.example.jejugudgo.domain.user.dto.response.UserInfoResponse;
import com.example.jejugudgo.domain.user.entity.Provider;
import com.example.jejugudgo.domain.user.entity.Role;
import com.example.jejugudgo.domain.user.entity.User;
import com.example.jejugudgo.domain.user.entity.UserTerms;
import com.example.jejugudgo.domain.user.repository.UserRepository;
import com.example.jejugudgo.domain.user.repository.UserTermsRepository;
import com.example.jejugudgo.global.jwt.token.TokenGenerator;
import com.example.jejugudgo.global.jwt.token.TokenType;
import com.example.jejugudgo.global.jwt.token.TokenUtil;
import com.example.jejugudgo.global.util.CookieUtil;
import com.example.jejugudgo.global.util.RandomNicknameUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicAuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserTermsRepository userTermsRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CookieUtil cookieUtil;
    private final TokenGenerator tokenGenerator;
    private final RandomNicknameUtil randomNicknameUtil;
    private final UserProfileService userProfileService;

    // 회원가입 메서드
    @Transactional
    public void signup(SignupRequest request) {
        // 1. 비밀번호 유효성 검사
        validatePassword(request.password());

        // 2. 사용자 정보 생성 및 저장
        User user = createUser(request);

        // 3. 약관 동의 처리
        handleTermsAgreements(request.terms(), user);
    }

    // 비밀번호 유효성 검사 메서드
    private void validatePassword(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&#]{8,20}$";
        if (!password.matches(passwordPattern)) {
            throw new IllegalArgumentException();
        }
    }

    // 사용자 생성 메서드
    private User createUser(SignupRequest request) {
        UserProfile userProfile = userProfileService.createProfile("default");

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
                .userProfile(userProfile)
                .build();

        return userRepository.save(user);
    }

    // 약관 동의 처리 메서드
    private void handleTermsAgreements(List<TermsAgreementRequest> termsAgreementRequests, User user) {
        for (TermsAgreementRequest agreementRequest : termsAgreementRequests) {
            UserTerms userTerms = UserTerms.builder()
                    .user(user)
                    .termsId(agreementRequest.termsId())
                    .isAgreed(agreementRequest.isAgreed())
                    .build();

            userTermsRepository.save(userTerms);
        }
    }

    public boolean checkEmailDuplicate(EmailRequest request) {
        return userRepository.findByEmail(request.email()).isPresent();
    }

    public UserInfoResponse loginAndGetUserInfo(LoginRequest request, HttpServletResponse response) {
        // 1. 유저 정보 조회
        User user = userRepository.findByEmailAndProvider(request.email(), Provider.BASIC)
                .orElseThrow(EntityNotFoundException::new);

        addTokenToCookie(user.getId(), response);
        authenticateUser(request);

        // 2. 비밀번호 검증
        if (!bCryptPasswordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException();
        }

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

    private void addTokenToCookie(Long userId, HttpServletResponse response) {
        String accessToken = tokenGenerator.generateToken(TokenType.ACCESS, String.valueOf(userId));
        String refreshToken = tokenGenerator.generateToken(TokenType.ACCESS, String.valueOf(userId));

        cookieUtil.setCookie("accessToken", accessToken, response);
        cookieUtil.setCookie("refreshToken", refreshToken, response);
    }

    private void authenticateUser(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
    }
}

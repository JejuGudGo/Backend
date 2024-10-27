package com.example.jejugudgo.domain.auth.basic.service;

import com.example.jejugudgo.domain.auth.basic.dto.request.LoginRequest;
import com.example.jejugudgo.domain.auth.basic.dto.request.SignupRequest;
import com.example.jejugudgo.domain.auth.terms.dto.request.TermsAgreementRequest;
import com.example.jejugudgo.domain.profile.entity.UserProfile;
import com.example.jejugudgo.domain.profile.service.UserProfileService;
import com.example.jejugudgo.domain.user.dto.response.UserInfoResponse;
import com.example.jejugudgo.domain.user.entity.Provider;
import com.example.jejugudgo.domain.user.entity.Role;
import com.example.jejugudgo.domain.user.entity.User;
import com.example.jejugudgo.domain.user.entity.UserTerms;
import com.example.jejugudgo.domain.user.event.UserCheckListEvent;
import com.example.jejugudgo.domain.user.repository.UserRepository;
import com.example.jejugudgo.domain.user.repository.UserTermsRepository;
import com.example.jejugudgo.global.exception.CustomException;
import com.example.jejugudgo.global.exception.entity.RetCode;
import com.example.jejugudgo.global.jwt.token.TokenGenerator;
import com.example.jejugudgo.global.util.RandomNicknameUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class BasicAuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserTermsRepository userTermsRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RandomNicknameUtil randomNicknameUtil;
    private final UserProfileService userProfileService;
    private final TokenGenerator tokenGenerator;

    private final ApplicationEventPublisher eventPublisher;


    private static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&#]{8,20}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    // 회원가입 메서드
    @Transactional
    public void signup(SignupRequest request) {
        // 1. 비밀번호 유효성 검사
        if (!isValidPassword(request.password())) {
            throw new CustomException(RetCode.RET_CODE06);
        }

        // 2. 이메일 중복 검사
        if (isEmailExists(request.email())) {
            throw new CustomException(RetCode.RET_CODE07);
        }

        // 3. 휴대폰 번호 형식 검증
        if (!isValidPhoneNumber(request.phoneNumber())) {
            throw new CustomException(RetCode.RET_CODE03);
        }

        // 4. 사용자 정보 생성 및 저장
        User user = createUser(request);

        // 5. 약관 동의 처리
        handleTermsAgreements(request.terms(), user);

        // 6. 체크리스트 생성
        eventPublisher.publishEvent(new UserCheckListEvent(user.getId()));
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("^010[0-9]{8}$");  // 010 + 8자리 숫자
    }

    private boolean isValidPassword(String password) {
        if (password == null) return false;
        return pattern.matcher(password).matches();
    }

    private boolean isEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    // API 엔드포인트용 이메일 중복 체크
    public void checkEmailDuplicate(String email) {
        if (isEmailExists(email)) {
            throw new CustomException(RetCode.RET_CODE07);  // 해당 이메일을 가진 유저가 존재합니다
        }
    }

    // 사용자 생성 메서드
    private User createUser(SignupRequest request) {
        try {
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
                    .deletedAt(LocalDateTime.of(1970, 1, 1, 0, 0))
                    .role(Role.USER)
                    .provider(Provider.BASIC)
                    .userProfile(userProfile)
                    .build();

            return userRepository.save(user);
        } catch (Exception e) {
            throw new CustomException(RetCode.RET_CODE99);
        }

    }

    // 약관 동의 처리 메서드
    private void handleTermsAgreements(List<TermsAgreementRequest> termsAgreementRequests, User user) {
        try {
            for (TermsAgreementRequest agreementRequest : termsAgreementRequests) {
                UserTerms userTerms = UserTerms.builder()
                        .user(user)
                        .termsId(agreementRequest.termsId())
                        .isAgreed(agreementRequest.isAgreed())
                        .build();

                userTermsRepository.save(userTerms);
            }
        } catch (Exception e) {
            throw new CustomException(RetCode.RET_CODE99);
        }
    }

    public UserInfoResponse loginAndGetUserInfo(LoginRequest request, HttpServletResponse response) {
        // 1. 유저 정보 조회
        User user = userRepository.findByEmailAndProvider(request.email(), Provider.BASIC)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE08));

        // 2. 비밀번호 검증
        if (!bCryptPasswordEncoder.matches(request.password(), user.getPassword())) {
            throw new CustomException(RetCode.RET_CODE09);
        }

        // 3. 인증 처리
        authenticateUser(request);

        // 4. 토큰 생성 및 헤더 추가
        String accessToken = addAccessTokenToHeader(user.getId(), response);

        // 5. 사용자 정보 반환
        return new UserInfoResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getNickname(),
                user.getCreatedAt(),
                user.getPhoneNumber(),
                user.getRole(),
                accessToken
        );
    }

    private void authenticateUser(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
    }

    private String addAccessTokenToHeader(Long userId, HttpServletResponse response) {
        String accessToken = tokenGenerator.generateToken(String.valueOf(userId));
        response.setHeader("Authorization", accessToken);
        return accessToken;
    }
}

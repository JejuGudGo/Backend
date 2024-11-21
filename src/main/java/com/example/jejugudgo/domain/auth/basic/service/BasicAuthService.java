package com.example.jejugudgo.domain.auth.basic.service;

import com.example.jejugudgo.domain.auth.basic.dto.request.LoginRequest;
import com.example.jejugudgo.domain.auth.basic.dto.request.SignupRequest;
import com.example.jejugudgo.domain.auth.basic.dto.response.SignupResponse;
import com.example.jejugudgo.domain.user.terms.dto.TermsAgreementRequest;
import com.example.jejugudgo.domain.auth.validation.PasswordValidation;
import com.example.jejugudgo.domain.auth.validation.PhoneValidation;
import com.example.jejugudgo.domain.auth.validation.UserValidation;
import com.example.jejugudgo.domain.user.myGudgo.profile.entity.UserProfile;
import com.example.jejugudgo.domain.user.myGudgo.profile.service.UserProfileService;
import com.example.jejugudgo.domain.auth.basic.dto.response.UserInfoResponse;
import com.example.jejugudgo.domain.user.user.entity.Provider;
import com.example.jejugudgo.domain.user.user.entity.Role;
import com.example.jejugudgo.domain.user.user.entity.User;
import com.example.jejugudgo.domain.user.terms.entity.UserTerms;
import com.example.jejugudgo.domain.user.checkList.event.UserCheckListEvent;
import com.example.jejugudgo.domain.user.user.repository.UserRepository;
import com.example.jejugudgo.domain.user.terms.repository.UserTermsRepository;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.jwt.token.TokenGenerator;
import com.example.jejugudgo.global.jwt.token.TokenUtil;
import com.example.jejugudgo.global.util.random.RandomNicknameUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
    private final PasswordValidation passwordValidation;
    private final PhoneValidation phoneValidation;
    private final UserValidation userValidation;
    private final TokenUtil tokenUtil;
    private final ApplicationEventPublisher eventPublisher;

    // 회원가입 메서드
    @Transactional
    public SignupResponse signup(SignupRequest request) {
        // 1. 비밀번호 유효성 검사
        String encodedPassword = passwordValidation.validatePassword(request.password());

        // 2. 이메일 중복 검사
        userValidation.validateEmail(request.email());

        // 3. 휴대폰 번호 형식 검증
        phoneValidation.validatePhoneNumber(request.phoneNumber());

        // 4. 사용자 정보 생성 및 저장
        User user = createUser(request, encodedPassword);

        // 5. 약관 동의 처리
        handleTermsAgreements(request.terms(), user);

        // 6. 체크리스트 생성
        eventPublisher.publishEvent(new UserCheckListEvent(user.getId()));

        SignupResponse response = new SignupResponse(user.getNickname());

        return response;
    }


    // 사용자 생성 메서드
    private User createUser(SignupRequest request, String encodedPassword) {
        try {
            UserProfile userProfile = userProfileService.createProfile("default");

            // 랜덤 닉네임 생성
            String nickname = randomNicknameUtil.set();

            User user = User.builder()
                    .email(request.email())
                    .password(encodedPassword)  // 비밀번호 암호화
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
    private void handleTermsAgreements(TermsAgreementRequest termsAgreementRequests, User user) {
        try {
            UserTerms userTerms = UserTerms.builder()
                    .user(user)
                    .isAgreed(termsAgreementRequests.isAgreed())
                    .agreedAt(termsAgreementRequests.agreedAt())
                    .build();

            userTermsRepository.save(userTerms);

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
                user.getUserProfile().getProfileImageUrl(),
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

    // 사용자 삭제 로직
    public void deleteUser(HttpServletRequest request) {
        try {
            // 1. 사용자 조회
            Long userId = tokenUtil.getUserIdFromHeader(request);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(RetCode.RET_CODE99));

            // 2. 사용자 삭제
            user = user.updateUserStatus();
            userRepository.save(user);

        } catch (ExpiredJwtException e) {
            throw new CustomException(RetCode.RET_CODE98);  // 토큰 만료
        } catch (Exception e) {
            throw new CustomException(RetCode.RET_CODE99);  // 기타 서버 오류
        }
    }
}

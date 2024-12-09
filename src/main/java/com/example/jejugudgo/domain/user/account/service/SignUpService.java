package com.example.jejugudgo.domain.user.account.service;

import com.example.jejugudgo.domain.user.account.dto.request.SignUpRequest;
import com.example.jejugudgo.domain.user.account.dto.request.TermAgreement;
import com.example.jejugudgo.domain.user.account.dto.response.SignUpResponse;
import com.example.jejugudgo.domain.user.athentication.signIn.dto.request.OauthRequest;
import com.example.jejugudgo.domain.user.checklist.event.UserCheckListEvent;
import com.example.jejugudgo.domain.user.common.entity.User;
import com.example.jejugudgo.domain.user.common.entity.UserProfile;
import com.example.jejugudgo.domain.user.common.enums.Provider;
import com.example.jejugudgo.domain.user.common.enums.Role;
import com.example.jejugudgo.domain.user.common.enums.UserStatus;
import com.example.jejugudgo.domain.user.common.repository.UserRepository;
import com.example.jejugudgo.domain.user.common.validation.ValidationManager;
import com.example.jejugudgo.domain.user.athentication.term.entity.UserTerm;
import com.example.jejugudgo.domain.user.athentication.term.repository.UserTermRepository;
import com.example.jejugudgo.global.data.term.entity.Term;
import com.example.jejugudgo.global.data.term.repository.TermRepository;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.util.random.RandomNicknameUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SignUpService {
    private final ValidationManager validationManager;
    private final ApplicationEventPublisher eventPublisher;
    private final RandomNicknameUtil randomNicknameUtil;
    private final UserProfileService userProfileService;
    private final UserRepository userRepository;
    private final UserTermRepository userTermRepository;
    private final TermRepository termRepository;

    private final String DEFAULT_IMAGE_URL = "default";
    private final int TERM_SIZE = 3;

    @Value("${spring.oauth.password}")
    private String DEFAULT_PASSWORD;

    public SignUpResponse signUp(SignUpRequest request) {
        String encodedPassword = validationManager.validatePasswordPattern(request.password());
        User user = createUser(request, encodedPassword);
        handleTermAgreement(request.termAgreement());
        eventPublisher.publishEvent(new UserCheckListEvent(user.getId()));

        return new SignUpResponse(
                user.getNickname()
        );
    }

    public void signUp(OauthRequest request, Provider provider) {
        User targetUser = userRepository.findByEmailAndProvider(request.email(), provider)
                .orElse(null);

        if (targetUser == null) {
            User user = createUser(request, provider);
            handleTermAgreement();
            eventPublisher.publishEvent(new UserCheckListEvent(user.getId()));
        }
    }

    private User createUser(SignUpRequest request, String encodedPassword) {
        try {
            UserProfile userProfile = userProfileService.create(DEFAULT_IMAGE_URL);
            String nickname = randomNicknameUtil.set();

            User user = User.builder()
                    .role(Role.USER)
                    .provider(Provider.BASIC)
                    .userStatus(UserStatus.ACTIVE)
                    .email(request.email())
                    .password(encodedPassword)
                    .nickname(nickname)
                    .name(request.name())
                    .createdAt(LocalDateTime.now())
                    .phoneNumber(request.phoneNumber())
                    .userProfile(userProfile)
                    .build();

            return userRepository.save(user);

        } catch (Exception e) {
            // TODO: 생성 실패 예외처리 따로 분류하기
            throw new CustomException(RetCode.RET_CODE99);
        }
    }

    private User createUser(OauthRequest request, Provider provider) {
        try {
            UserProfile userProfile = userProfileService.create(
                    request.profileImgUrl() == null ? DEFAULT_IMAGE_URL : request.profileImgUrl()
            );
            String encodedPassword = validationManager.validatePasswordPattern(DEFAULT_PASSWORD);
            String nickname = randomNicknameUtil.set();

            User user = User.builder()
                    .role(Role.USER)
                    .userStatus(UserStatus.ACTIVE)
                    .provider(provider)
                    .email(request.email())
                    .password(encodedPassword)
                    .nickname(nickname)
                    .createdAt(LocalDateTime.now())
                    .userProfile(userProfile)
                    .build();

            return userRepository.save(user);

        } catch (Exception e) {
            // TODO: 생성 실패 예외처리 따로 분류하기
            throw new CustomException(RetCode.RET_CODE99);
        }
    }

    private void handleTermAgreement() {
        Long index = 1L;
        for (int i = 1; i <= TERM_SIZE; i ++) {
            Term term = termRepository.findById(index)
                    .orElseThrow(() -> new CustomException(RetCode.RET_CODE99));

            UserTerm userTerm = UserTerm.builder()
                    .isAgreed(true)
                    .updatedAt(LocalDateTime.now().toString()) // TODO: 형식 통일
                    .term(term)
                    .build();

            userTermRepository.save(userTerm);
            index ++;
        }
    }

    private void handleTermAgreement(TermAgreement agreement) {
        Long index = 1L;
        for (int i = 1; i <= TERM_SIZE; i ++) {
            Term term = termRepository.findById(index)
                    .orElseThrow(() -> new CustomException(RetCode.RET_CODE99));

            UserTerm userTerm = UserTerm.builder()
                    .isAgreed(
                            i == TERM_SIZE ? agreement.isAgree() : true
                    )
                    .updatedAt(
                            i == TERM_SIZE ? agreement.agreedAt() : LocalDateTime.now().toString()
                    ) // TODO: 형식 통일
                    .term(term)
                    .build();

            userTermRepository.save(userTerm);
            index ++;
        }
    }
}

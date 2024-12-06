package com.example.jejugudgo.domain.user.account.service;

import com.example.jejugudgo.domain.user.account.dto.request.PasswordUpdateRequest;
import com.example.jejugudgo.domain.user.account.dto.response.UserProfileResponse;
import com.example.jejugudgo.domain.user.common.entity.User;
import com.example.jejugudgo.domain.user.common.entity.UserProfile;
import com.example.jejugudgo.domain.user.common.enums.Provider;
import com.example.jejugudgo.domain.user.common.repository.UserProfileRepository;
import com.example.jejugudgo.domain.user.common.repository.UserRepository;
import com.example.jejugudgo.domain.user.common.validation.ValidationManager;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.jwt.token.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final ValidationManager validationManager;
    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final TokenUtil tokenUtil;

    public UserProfile create(String profileImageUrl) {
        UserProfile userProfile = UserProfile.builder()
                .walkingTime(LocalTime.of(0, 0))
                .walkingCount(0L)
                .profileImageUrl(profileImageUrl)
                .walkingTime(LocalTime.of(0, 0))
                .build();

        userProfileRepository.save(userProfile);

        return userProfile;
    }

    // 마이걷고 진입시 상단에 나오는 프로필
    public UserProfileResponse get(HttpServletRequest httpRequest) {
        Long userId = tokenUtil.getUserIdFromHeader(httpRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        return new UserProfileResponse(
                user.getUserProfile().getWalkingTime(),
                user.getUserProfile().getWalkingCount(),
                user.getUserProfile().getProfileImageUrl(),
                user.getNickname(),
                user.getUserProfile().getBadgeCount()
        );
    }

    public void updatePassword(PasswordUpdateRequest request) {
        User user = userRepository.findByEmailAndProvider(request.email(), Provider.BASIC)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE08));

        String encodedPassword = validationManager.validatePasswordPattern(request.password());
        user = user.updatePassword(encodedPassword);

        userRepository.save(user);
    }
}

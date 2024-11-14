package com.example.jejugudgo.domain.profile.service;

import com.example.jejugudgo.domain.profile.dto.request.UserProfileUpdateRequest;
import com.example.jejugudgo.domain.profile.dto.response.UserProfileUpdateResponse;
import com.example.jejugudgo.domain.profile.entity.UserProfile;
import com.example.jejugudgo.domain.profile.repository.UserProfileRepository;
import com.example.jejugudgo.domain.user.entity.User;
import com.example.jejugudgo.domain.user.repository.UserRepository;
import com.example.jejugudgo.global.exception.CustomException;
import com.example.jejugudgo.global.exception.entity.RetCode;
import com.example.jejugudgo.global.jwt.token.TokenUtil;
import com.example.jejugudgo.global.util.ImageUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final TokenUtil tokenUtil;
    private final ImageUtil imageUtil;

    public UserProfile createProfile(String profileImageUrl) {
        UserProfile userProfile = UserProfile.builder()
                .badgeCount(0L)
                .walkingCount(0L)
                .walkingTime(LocalTime.of(0, 0))
                .profileImageUrl(profileImageUrl)
                .build();

        userProfileRepository.save(userProfile);

        return userProfile;
    }
    public UserProfileUpdateResponse getProfileForUpdate(HttpServletRequest request) {
        Long userId = tokenUtil.getUserIdFromHeader(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));
        String profileImageUrl = user.getUserProfile().getProfileImageUrl();
        return new UserProfileUpdateResponse(
                user.getNickname(),
                profileImageUrl
        );
    }

    public UserProfileUpdateResponse updateProfile(UserProfileUpdateRequest updateRequest, HttpServletRequest servletRequest) throws Exception {
        Long userId = tokenUtil.getUserIdFromHeader(servletRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));
        UserProfile userProfile = user.getUserProfile();

        if (updateRequest.nickname() != null) {
            user = user.updateNickname(updateRequest.nickname());
            userRepository.save(user);
        }

        MultipartFile profileImage = updateRequest.profileImage();
        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = imageUtil.saveImage(userId, profileImage, "profile").toString();
            userProfile = userProfile.updateProfileImageUrl(imageUrl);
            userProfileRepository.save(userProfile);
        }

        return new UserProfileUpdateResponse(
                user.getNickname(),
                userProfile.getProfileImageUrl()
        );
    }
}

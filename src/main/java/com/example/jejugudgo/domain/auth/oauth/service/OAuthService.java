package com.example.jejugudgo.domain.auth.oauth.service;

import com.example.jejugudgo.domain.auth.basic.dto.request.LoginRequest;
import com.example.jejugudgo.domain.auth.basic.dto.response.SignupResponse;
import com.example.jejugudgo.domain.auth.basic.service.BasicAuthService;
import com.example.jejugudgo.domain.auth.oauth.repository.OAuthRequest;
import com.example.jejugudgo.domain.profile.entity.UserProfile;
import com.example.jejugudgo.domain.profile.service.UserProfileService;
import com.example.jejugudgo.domain.user.dto.response.UserInfoResponse;
import com.example.jejugudgo.domain.user.entity.Provider;
import com.example.jejugudgo.domain.user.entity.Role;
import com.example.jejugudgo.domain.user.entity.User;
import com.example.jejugudgo.domain.user.repository.UserRepository;
import com.example.jejugudgo.global.util.RandomNicknameUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final UserRepository userRepository;
    private final UserProfileService userProfileService;
    private final RandomNicknameUtil randomNicknameUtil;
    private final BasicAuthService basicAuthService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserInfoResponse oauthLogin(String provider, OAuthRequest request, HttpServletResponse response) {
        User user = userRepository.findByProviderAndOauthUserId(validateOauthProvider(provider), request.oauthUserId())
                .orElse(null);

        if (user == null) {
            signup(provider, request);

        } else {
            LoginRequest loginRequest = new LoginRequest(
                    user.getEmail(),
                    provider
            );

            return basicAuthService.loginAndGetUserInfo(loginRequest, response);
        }

        return null;
    }

    private Provider validateOauthProvider(String provider) {
        if (provider.equals("google")) return Provider.GOOGLE;
        else if (provider.equals("kakao")) return Provider.KAKAO;
        else if (provider.equals("apple")) return Provider.APPLE;
        return null;
    }

    private void signup(String provider, OAuthRequest request) {
        String profileImgUrl =
                request.profileImgUrl().isEmpty()
                        ? "default" : request.profileImgUrl();

        UserProfile userProfile = userProfileService.createProfile(profileImgUrl);

        String nickname = randomNicknameUtil.set();

        User user = User.builder()
                .password(bCryptPasswordEncoder.encode(provider))
                .userProfile(userProfile)
                .oauthUserId(request.oauthUserId())
                .nickname(nickname)
                .provider(validateOauthProvider(provider))
                .createdAt(LocalDateTime.now())
                .role(Role.USER)
                .email(request.email())
                .build();

        userRepository.save(user);
    }
}

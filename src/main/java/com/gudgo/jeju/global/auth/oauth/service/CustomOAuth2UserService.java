package com.gudgo.jeju.global.auth.oauth.service;

import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.auth.oauth.entity.CustomOAuth2User;
import com.gudgo.jeju.global.auth.oauth.entity.OAuth2UserInfo;
import com.gudgo.jeju.global.auth.oauth.entity.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final OAuth2SignupService oAuth2SignupService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);
        log.info("============================================================================");
        log.info("getAttributes: {}", oAuth2User.getAttributes());
        log.info("============================================================================");

        String provider = request.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuthUserInfo(provider, oAuth2User.getAttributes());
        Optional<User> user = userRepository.findByEmailAndProvider(provider, oAuth2UserInfo.getEmail());

        if (user.isPresent()) {
            return new CustomOAuth2User(user.get(), oAuth2User.getAttributes());

        }
//        else {
//            User newUser = oAuth2SignupService.signup(provider, oAuth2UserInfo);
//            return new CustomOAuth2User(newUser, oAuth2User.getAttributes());
//        }
        return new CustomOAuth2User(user.get(), oAuth2User.getAttributes());
    }
}

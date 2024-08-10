package com.gudgo.jeju.global.auth.oauth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gudgo.jeju.domain.profile.entity.Profile;
import com.gudgo.jeju.domain.profile.repository.ProfileRepository;
import com.gudgo.jeju.domain.user.entity.Role;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import com.gudgo.jeju.global.auth.oauth.entity.KakaoUserInfo;
import com.gudgo.jeju.global.auth.oauth.entity.OAuth2UserInfoFactory;
import com.gudgo.jeju.global.jwt.token.TokenGenerator;
import com.gudgo.jeju.global.jwt.token.TokenType;
import com.gudgo.jeju.global.security.CustomUserDetails;
import com.gudgo.jeju.global.util.RandomNicknameUtil;
import com.gudgo.jeju.global.util.RandomNumberUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class KakaoService {
    private final UserRepository userRepository;

    private final RandomNicknameUtil randomNicknameUtil;
    private final TokenGenerator tokenGenerator;
    private final ProfileRepository profileRepository;

    private final WebClient.Builder webClientBuilder;
    private final RandomNumberUtil randomNumberUtil;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String REDIRECT_URI;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String TOKEN_URI;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String USER_INFO_URI;

    public KakaoUserInfo login(String code, HttpServletResponse response) throws JsonProcessingException {

        // 1. 인가 코드로 액세스 토큰 요청
        String acceessToken = getAccessToken(code);

        // 2. 토큰으로 카카오 API 호출
        KakaoUserInfo userInfo = getKakaoUserInfo(acceessToken);
//        KakaoUserInfo userInfo = getKakaoUserInfo(code);

        // 3. 카카오 ID로 회원가입 처리
        User kakaoUser = registerIfNeed(userInfo);

        // 4. 강제 로그인 처리
        Authentication authentication = forceLogin(kakaoUser);

        // 5. Response Header에 JWT 토큰 추가
        KakaoUserAuthorizationInput(kakaoUser, response);
        return userInfo;
    }


    private String getAccessToken(String code) throws JsonProcessingException {

        WebClient webClient = webClientBuilder.build();

        // HTTP 요청을 보내 액세스 토큰을 가져옵니다.
        String responseBody = webClient.post()
                .uri(TOKEN_URI)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(buildTokenRequestBody(code))
                .retrieve()
                .bodyToMono(String.class)
                .block(); // 동기 방식으로 결과를 기다림

        // JSON 응답에서 액세스 토큰을 추출
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }



    private KakaoUserInfo getKakaoUserInfo(String accessToken) throws JsonProcessingException {

        WebClient webClient = webClientBuilder.build();

        // HTTP 요청을 보내 사용자 정보를 가져옵니다.
        String responseBody = webClient.get()
                .uri(USER_INFO_URI)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // 동기 방식으로 결과를 기다림

        // JSON 응답을 Map으로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> attributes = objectMapper.readValue(responseBody, Map.class);

        // OAuth2UserInfoFactory를 사용하여 KakaoUserInfo 객체를 생성
        return (KakaoUserInfo) OAuth2UserInfoFactory.getOAuthUserInfo("kakao", attributes);
    }

    private MultiValueMap<String, String> buildTokenRequestBody(String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", CLIENT_ID);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("code", code);
        body.add("client_secret", CLIENT_SECRET);
        return body;
    }

    private User registerIfNeed(KakaoUserInfo kakaoUserInfo) {
        User user = userRepository.findByEmail(kakaoUserInfo.getEmail())
                .orElseGet(() -> createUser(kakaoUserInfo));
        return user;
    }

    private User createUser(KakaoUserInfo kakaoUserInfo) {

        Profile profile = Profile.builder()
                .profileImageUrl(kakaoUserInfo.getProfile())
                .build();

        profileRepository.save(profile);

        User newUser = User.builder()
                .email(kakaoUserInfo.getEmail())
                .name(kakaoUserInfo.getName())
                .nickname(randomNicknameUtil.set())
                .numberTag(randomNumberUtil.set())
                .createdAt(LocalDateTime.now())
                .role(Role.USER)
                .profile(profile)
                .provider("kakao")
                .build();

        User savedUser = userRepository.save(newUser);

        return savedUser;
    }

    private Authentication forceLogin(User user) {
        UserDetails userDetails = new CustomUserDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    private void KakaoUserAuthorizationInput(User kakaoUser, HttpServletResponse response) {
        // response header에 token 추가
        String token = tokenGenerator.generateToken(TokenType.ACCESS, String.valueOf(kakaoUser.getId()));
        response.setHeader("Authorization", "Bearer " + token);
    }
}

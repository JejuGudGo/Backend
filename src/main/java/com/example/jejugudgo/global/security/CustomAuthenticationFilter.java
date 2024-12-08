package com.example.jejugudgo.global.security;

import com.example.jejugudgo.domain.user.account.service.SignUpService;
import com.example.jejugudgo.domain.user.athentication.signIn.dto.request.OauthRequest;
import com.example.jejugudgo.domain.user.athentication.signIn.dto.request.SignInRequest;
import com.example.jejugudgo.domain.user.athentication.signIn.validation.SignInValidation;
import com.example.jejugudgo.domain.user.common.entity.User;
import com.example.jejugudgo.domain.user.common.enums.Provider;
import com.example.jejugudgo.domain.user.common.enums.UserStatus;
import com.example.jejugudgo.domain.user.common.repository.UserRepository;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Value("${spring.oauth.password}")
    private String DEFAULT_PASSWORD;
    private final SignUpService signUpService;
    private final UserRepository userRepository;

    private static final Map<String, Provider> URL_PROVIDER = Map.of(
            "/api/v1/signin", Provider.BASIC,
            "/api/v1/signin/kakao", Provider.KAKAO,
            "/api/v1/signin/google", Provider.GOOGLE,
            "/api/v1/signin/apple", Provider.APPLE
    );

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, SignUpService signUpService, UserRepository userRepository) {
        super.setAuthenticationManager(authenticationManager);
        this.signUpService = signUpService;
        this.userRepository = userRepository;

        RequestMatcher matcher = new OrRequestMatcher(
                URL_PROVIDER.keySet().stream()
                        .map(AntPathRequestMatcher::new)
                        .toArray(RequestMatcher[]::new)
        );
        setRequiresAuthenticationRequestMatcher(matcher);
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest httpRequest, final HttpServletResponse httpResponse) {
        try {
            Provider provider = getProvider(httpRequest);
            ObjectMapper objectMapper = new ObjectMapper();

            if (provider == Provider.BASIC) {
                SignInRequest signInRequest = objectMapper.readValue(httpRequest.getInputStream(), SignInRequest.class);
                logRequest(signInRequest.email(), provider);

                return this.getAuthenticationManager().authenticate(
                        new CustomAuthenticationToken(signInRequest.email(), signInRequest.password(), provider)
                );
            } else {
                OauthRequest oauthRequest = objectMapper.readValue(httpRequest.getInputStream(), OauthRequest.class);
                logRequest(oauthRequest.email(), provider);
                signUpService.signUp(oauthRequest, provider);

                return this.getAuthenticationManager().authenticate(
                        new CustomAuthenticationToken(oauthRequest.email(), DEFAULT_PASSWORD, provider)
                );
            }
        } catch (CustomException | IOException e) {
            httpRequest.setAttribute("customException", e);
            throw new InsufficientAuthenticationException(e.getMessage());
        }
    }

    private Provider getProvider(HttpServletRequest httpRequest) {
        return URL_PROVIDER.get(httpRequest.getRequestURI());
    }

    private void logRequest(String email, Provider provider) {
        log.info("===========================================================================");
        log.info("SignIn user's email : {}", email);
        log.info("SignIn user's provider : {}", provider);
        log.info("===========================================================================");
    }

    private User findUser(String email, Provider provider) {
        User user = userRepository.findByEmailAndProvider(email, provider)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE07));

        if (user.getUserStatus() == UserStatus.DELETED)
            throw new CustomException(RetCode.RET_CODE12);

        return user;
    }
}
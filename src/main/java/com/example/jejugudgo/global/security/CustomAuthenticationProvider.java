package com.example.jejugudgo.global.security;

import com.example.jejugudgo.domain.user.athentication.signIn.validation.SignInValidation;
import com.example.jejugudgo.domain.user.common.entity.User;
import com.example.jejugudgo.domain.user.common.enums.Provider;
import com.example.jejugudgo.domain.user.common.enums.UserStatus;
import com.example.jejugudgo.domain.user.common.repository.UserRepository;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final CustomUserDetailsService customUserDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final SignInValidation signInValidation;

    @Override
    public Authentication authenticate(Authentication authentication) {
        CustomAuthenticationToken token = (CustomAuthenticationToken) authentication;
        String email = token.getName();
        String password = (String) token.getCredentials();
        Provider provider = token.getProvider();
        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByEmailAndProvider(email, provider);

        log.info("======================================================");
        log.info("[Authentication] email={}, provider={}", email, provider);
        log.info("User details: {}", userDetails);
        log.info("======================================================");

        User user = findUser(email, provider);

        if (user.getUserStatus() == UserStatus.DELETED)
            throw new CustomException(RetCode.RET_CODE12);

        if (!bCryptPasswordEncoder.matches(password, userDetails.getPassword())) {
            signInValidation.validateSignInStatus(user);
        }
        return new CustomAuthenticationToken(userDetails, password, provider);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private User findUser(String email, Provider provider) {
        return userRepository.findByEmailAndProvider(email, provider)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE07));
    }
}
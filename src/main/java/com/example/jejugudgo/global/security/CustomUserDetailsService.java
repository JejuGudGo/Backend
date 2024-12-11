package com.example.jejugudgo.global.security;

import com.example.jejugudgo.domain.user.common.entity.User;
import com.example.jejugudgo.domain.user.common.enums.Provider;
import com.example.jejugudgo.domain.user.common.repository.UserRepository;
import com.example.jejugudgo.global.exception.enums.RetCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService {
    private final UserRepository userRepository;

    public UserDetails loadUserByIdAndProvider(String id, Provider provider) {
        User user;

        if (provider.equals(Provider.BASIC))
            user = userRepository.findByEmailAndProvider(id, provider)
                .orElseThrow(() -> new UsernameNotFoundException(RetCode.RET_CODE08.getMessage()));
        else
            user = userRepository.findByOauthUserIdAndProvider(id, provider)
                    .orElseThrow(() -> new UsernameNotFoundException(RetCode.RET_CODE97.getMessage()));

        return new CustomUserDetails(user);
    }
}
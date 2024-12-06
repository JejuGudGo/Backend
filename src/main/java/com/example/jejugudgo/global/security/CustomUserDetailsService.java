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

    public UserDetails loadUserByEmailAndProvider(String email, Provider provider) {
        User user = userRepository.findByEmailAndProvider(email, provider)
                .orElseThrow(() -> new UsernameNotFoundException(RetCode.RET_CODE08.getMessage()));

        return new CustomUserDetails(user);
    }
}
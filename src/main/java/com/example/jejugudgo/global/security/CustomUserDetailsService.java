package com.example.jejugudgo.global.security;

import com.example.jejugudgo.domain.user.entity.Provider;
import com.example.jejugudgo.domain.user.entity.User;
import com.example.jejugudgo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmailAndProvider(email, Provider.BASIC)
                .orElseThrow(() -> new UsernameNotFoundException("AUTH_01"));

        return new CustomUserDetails(user);
    }
}
package com.example.jejugudgo.domain.user.service;

import com.example.jejugudgo.domain.user.dto.request.PasswordUpdateRequest;
import com.example.jejugudgo.domain.user.entity.User;
import com.example.jejugudgo.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void updatePassword(PasswordUpdateRequest request) {
        User user = userRepository.findByEmailAndProvider(request.email(), "basic")
                .orElseThrow(EntityNotFoundException::new);

        user.updatePassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);
    }
}

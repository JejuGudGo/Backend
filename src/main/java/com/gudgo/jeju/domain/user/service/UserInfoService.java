package com.gudgo.jeju.domain.user.service;


import com.gudgo.jeju.domain.user.dto.UserInfoResponseDto;
import com.gudgo.jeju.domain.user.entity.Role;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserInfoService {
    private final UserRepository userRepository;

    public UserInfoResponseDto get(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return new UserInfoResponseDto(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getName(),
                user.getNumberTag(),
                user.getProfile().getProfileImageUrl(),
                user.getRole()
        );
    }

    public void updateUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        User updatedUser = user.withRole(Role.AUTHOR);
        userRepository.save(updatedUser);
    }
}

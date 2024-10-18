package com.example.jejugudgo.domain.profile.service;

import com.example.jejugudgo.domain.profile.entity.UserProfile;
import com.example.jejugudgo.domain.profile.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;

    public UserProfile createProfile(String profileImageUrl) {
        UserProfile userProfile = UserProfile.builder()
                .badgeCount(0L)
                .walkingCount(0L)
                .walkingTime(LocalTime.of(0, 0))
                .profileImageUrl(profileImageUrl)
                .build();

        userProfileRepository.save(userProfile);

        return userProfile;
    }
}

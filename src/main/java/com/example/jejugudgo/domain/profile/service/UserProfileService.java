package com.example.jejugudgo.domain.profile.service;

import com.example.jejugudgo.domain.profile.entity.UserProfile;
import com.example.jejugudgo.domain.profile.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;

    public UserProfile createProfile(String profileImageUrl) {
        UserProfile userProfile = UserProfile.builder()
                .profileImageUrl(profileImageUrl)
                .build();

        userProfileRepository.save(userProfile);

        return userProfile;
    }
}

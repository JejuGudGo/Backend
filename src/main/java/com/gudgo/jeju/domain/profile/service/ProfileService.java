package com.gudgo.jeju.domain.profile.service;

import com.gudgo.jeju.domain.profile.entity.Profile;
import com.gudgo.jeju.domain.profile.repository.ProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    @Transactional
    public void incrementBadgeCount(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(EntityNotFoundException::new);

        Long newBadgeCount = profile.getBadgeCount() + 1;
        Profile updatedProfile = profile.withBadgeCount(newBadgeCount);

        profileRepository.save(updatedProfile);
    }
}

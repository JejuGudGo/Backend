package com.gudgo.jeju.domain.profile.repository;

import com.gudgo.jeju.domain.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}

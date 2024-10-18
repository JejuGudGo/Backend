package com.example.jejugudgo.domain.profile.repository;

import com.example.jejugudgo.domain.profile.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}

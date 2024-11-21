package com.example.jejugudgo.domain.user.myGudgo.profile.repository;

import com.example.jejugudgo.domain.user.myGudgo.profile.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}

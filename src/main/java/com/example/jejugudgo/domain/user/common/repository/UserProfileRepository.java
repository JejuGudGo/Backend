package com.example.jejugudgo.domain.user.common.repository;

import com.example.jejugudgo.domain.user.common.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile,String> {
}

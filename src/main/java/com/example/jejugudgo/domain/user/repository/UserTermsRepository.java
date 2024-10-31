package com.example.jejugudgo.domain.user.repository;

import com.example.jejugudgo.domain.user.entity.User;
import com.example.jejugudgo.domain.user.entity.UserTerms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTermsRepository extends JpaRepository<UserTerms, Long> {
    Optional<UserTerms> findByUser(User user);
}

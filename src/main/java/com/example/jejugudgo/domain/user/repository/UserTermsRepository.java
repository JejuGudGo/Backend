package com.example.jejugudgo.domain.user.repository;

import com.example.jejugudgo.domain.user.entity.UserTerms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTermsRepository extends JpaRepository<UserTerms, Long> {
}

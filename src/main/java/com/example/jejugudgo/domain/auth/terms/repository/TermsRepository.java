package com.example.jejugudgo.domain.auth.terms.repository;

import com.example.jejugudgo.domain.auth.terms.entity.Terms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermsRepository extends JpaRepository<Terms, Long> {
}

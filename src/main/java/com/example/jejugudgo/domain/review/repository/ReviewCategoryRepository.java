package com.example.jejugudgo.domain.review.repository;

import com.example.jejugudgo.domain.review.entity.ReviewCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewCategoryRepository extends JpaRepository<ReviewCategory, Long> {
}

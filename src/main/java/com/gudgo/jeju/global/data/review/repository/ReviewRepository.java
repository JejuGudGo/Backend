package com.gudgo.jeju.global.data.review.repository;

import com.gudgo.jeju.global.data.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByCode(String code);

}

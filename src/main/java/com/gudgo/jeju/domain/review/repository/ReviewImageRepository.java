package com.gudgo.jeju.domain.review.repository;

import com.gudgo.jeju.domain.review.entity.Review;
import com.gudgo.jeju.domain.review.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
    List<ReviewImage> findByReview(Review review);
}

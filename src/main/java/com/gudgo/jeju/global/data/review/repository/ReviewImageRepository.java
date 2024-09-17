package com.gudgo.jeju.global.data.review.repository;

import com.gudgo.jeju.global.data.review.entity.Review;
import com.gudgo.jeju.global.data.review.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
    List<ReviewImage> findByReview(Review review);
}

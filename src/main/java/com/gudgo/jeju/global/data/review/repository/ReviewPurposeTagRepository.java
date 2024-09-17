package com.gudgo.jeju.global.data.review.repository;

import com.gudgo.jeju.global.data.review.entity.Review;
import com.gudgo.jeju.global.data.review.entity.ReviewPurposeTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewPurposeTagRepository extends JpaRepository<ReviewPurposeTag, Long> {
    List<ReviewPurposeTag> findByReview(Review review);
}

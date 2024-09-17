package com.gudgo.jeju.domain.review.repository;

import com.gudgo.jeju.domain.review.entity.Review;
import com.gudgo.jeju.domain.review.entity.ReviewPurposeTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewPurposeTagRepository extends JpaRepository<ReviewPurposeTag, Long> {
    List<ReviewPurposeTag> findByReview(Review review);
}

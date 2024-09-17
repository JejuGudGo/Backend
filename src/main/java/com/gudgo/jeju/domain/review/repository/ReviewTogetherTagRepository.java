package com.gudgo.jeju.domain.review.repository;

import com.gudgo.jeju.domain.review.entity.ReviewTogetherTag;
import com.gudgo.jeju.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewTogetherTagRepository extends JpaRepository<ReviewTogetherTag, Long> {
    List<ReviewTogetherTag> findByReview(Review review);
}

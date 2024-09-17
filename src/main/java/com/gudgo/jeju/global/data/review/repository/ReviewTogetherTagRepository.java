package com.gudgo.jeju.global.data.review.repository;

import com.gudgo.jeju.global.data.review.entity.Review;
import com.gudgo.jeju.global.data.review.entity.ReviewTogetherTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewTogetherTagRepository extends JpaRepository<ReviewTogetherTag, Long> {
    List<ReviewTogetherTag> findByReview(Review review);
}

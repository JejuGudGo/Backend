package com.gudgo.jeju.domain.review.repository;

import com.gudgo.jeju.domain.review.entity.Review;
import com.gudgo.jeju.domain.review.entity.ReviewTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewTagRepository extends JpaRepository<ReviewTag, Long> {
    List<ReviewTag> findByReview(Review review);
}

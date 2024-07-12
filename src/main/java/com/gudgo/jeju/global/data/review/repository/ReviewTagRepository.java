package com.gudgo.jeju.global.data.review.repository;

import com.gudgo.jeju.global.data.review.entity.ReviewTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewTagRepository extends JpaRepository<ReviewTag, Long> {
    List<ReviewTag> findByReviewId(Long reviewId);
}

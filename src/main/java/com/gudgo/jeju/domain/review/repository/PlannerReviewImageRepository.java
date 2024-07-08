package com.gudgo.jeju.domain.review.repository;

import com.gudgo.jeju.domain.review.entity.PlannerReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlannerReviewImageRepository extends JpaRepository<PlannerReviewImage, Long> {
    List<PlannerReviewImage> findByPlannerReviewId(Long reviewId);
}

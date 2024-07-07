package com.gudgo.jeju.domain.review.repository;

import com.gudgo.jeju.domain.review.entity.PlannerReviewCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlannerReviewCategoryRepository extends JpaRepository<PlannerReviewCategory, Long> {
    List<PlannerReviewCategory> findByPlannerReviewId(Long plannerId);
}

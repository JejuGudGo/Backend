package com.gudgo.jeju.domain.planner.review.repository;

import com.gudgo.jeju.domain.planner.review.entity.PlannerReviewCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlannerReviewCategoryRepository extends JpaRepository<PlannerReviewCategory, Long> {
    List<PlannerReviewCategory> findByPlannerReviewId(Long plannerId);
}

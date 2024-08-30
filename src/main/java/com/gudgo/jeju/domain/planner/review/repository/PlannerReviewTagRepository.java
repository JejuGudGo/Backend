package com.gudgo.jeju.domain.planner.review.repository;

import com.gudgo.jeju.domain.planner.review.entity.PlannerReviewTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlannerReviewTagRepository extends JpaRepository<PlannerReviewTag, Long> {

    List<PlannerReviewTag> findByPlannerReviewCategoryId(Long categoryId);
}

package com.gudgo.jeju.domain.planner.review.repository;

import com.gudgo.jeju.domain.planner.review.entity.PlannerReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlannerReviewRepository extends JpaRepository<PlannerReview, Long> {

    List<PlannerReview> findByPlannerCourseOriginalCourseIdAndIsDeletedFalse(Long originalCourseId);

}

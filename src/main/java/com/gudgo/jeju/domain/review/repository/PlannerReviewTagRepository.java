package com.gudgo.jeju.domain.review.repository;

import com.gudgo.jeju.domain.review.entity.PlannerReviewTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlannerReviewTagRepository extends JpaRepository<PlannerReviewTag, Long> {

    List<PlannerReviewTag> findByCategoryId(Long categoryId);
}

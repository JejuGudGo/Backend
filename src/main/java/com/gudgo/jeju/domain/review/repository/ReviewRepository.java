package com.gudgo.jeju.domain.review.repository;

import com.gudgo.jeju.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByPlannerCourseOriginalCourseIdAndIsDeletedFalse(Long originalCourseId);

    int countByUserId(Long userId);

}
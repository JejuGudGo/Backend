package com.gudgo.jeju.global.data.review.repository;

import com.gudgo.jeju.global.data.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByPlannerCourseOriginalCourseIdAndIsDeletedFalse(Long originalCourseId);

}

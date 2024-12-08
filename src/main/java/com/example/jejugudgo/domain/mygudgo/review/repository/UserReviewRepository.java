package com.example.jejugudgo.domain.mygudgo.review.repository;

import com.example.jejugudgo.domain.course.common.enums.CourseType;
import com.example.jejugudgo.domain.mygudgo.review.entity.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserReviewRepository extends JpaRepository<UserReview, Long> {
    List<UserReview> findAllByReviewTypeAndTargetId(CourseType courseType, Long targetId);
}

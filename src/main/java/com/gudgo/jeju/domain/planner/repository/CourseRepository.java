package com.gudgo.jeju.domain.planner.repository;

import com.gudgo.jeju.domain.planner.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findAllByIsDeletedFalse();

    List<Course> findByUserIdAndIsDeletedFalse(Long userId);

    Course findByPostId(Long postId);

//    List<Course> findByUserId(Long userid);
}

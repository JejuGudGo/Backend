package com.gudgo.jeju.domain.course.repository;

import com.gudgo.jeju.domain.course.entity.Course;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findAllByIsDeletedFalse();

    List<Course> findByUserIdAndIsDeletedFalse(Long userId);

//    List<Course> findByUserId(Long userid);
}

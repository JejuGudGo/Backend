package com.gudgo.jeju.domain.planner.course.repository;

import com.gudgo.jeju.domain.planner.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}

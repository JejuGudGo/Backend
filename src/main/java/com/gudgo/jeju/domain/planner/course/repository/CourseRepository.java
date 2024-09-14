package com.gudgo.jeju.domain.planner.course.repository;

import com.gudgo.jeju.domain.planner.course.entity.Course;
import com.gudgo.jeju.domain.planner.course.entity.CourseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT c FROM Course c WHERE c.id = c.originalCourseId")
    List<Course> findOriginalCourses();
    boolean existsByTypeAndOlleCourseId(CourseType type, Long olleCourseId);
    long countByType(CourseType type);

}

package com.example.jejugudgo.domain.course.repository;

import com.example.jejugudgo.domain.course.entity.JejuOlleCourse;
import com.example.jejugudgo.domain.course.entity.JejuOlleCourseSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JejuOlleCourseRepository extends JpaRepository<JejuOlleCourse, Long> {
}

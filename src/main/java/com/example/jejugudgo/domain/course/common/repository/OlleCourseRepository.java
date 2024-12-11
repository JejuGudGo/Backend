package com.example.jejugudgo.domain.course.common.repository;

import com.example.jejugudgo.domain.course.common.entity.OlleCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OlleCourseRepository extends JpaRepository<OlleCourse, Long> {
    Optional<OlleCourse> findByTitle(String title);
}

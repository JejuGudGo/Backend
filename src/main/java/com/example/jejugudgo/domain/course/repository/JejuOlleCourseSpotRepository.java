package com.example.jejugudgo.domain.course.repository;

import com.example.jejugudgo.domain.course.entity.JejuOlleCourseSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JejuOlleCourseSpotRepository extends JpaRepository<JejuOlleCourseSpot, Long> {
    Optional<JejuOlleCourseSpot> findByJejuOlleCourseIdOrderByIdAsc(Long courseId);

    Optional<JejuOlleCourseSpot> findByJejuOlleCourseIdOrderByIdDesc(Long courseId);

}

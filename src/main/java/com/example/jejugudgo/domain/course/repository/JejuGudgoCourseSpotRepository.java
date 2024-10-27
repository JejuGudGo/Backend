package com.example.jejugudgo.domain.course.repository;

import com.example.jejugudgo.domain.course.entity.JejuGudgoCourseSpot;
import com.example.jejugudgo.domain.course.entity.JejuOlleCourseSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JejuGudgoCourseSpotRepository extends JpaRepository<JejuGudgoCourseSpot, Long> {
    Optional<JejuGudgoCourseSpot> findByJejuGudgoCourseIdOrderByIdAsc(Long courseId);

    Optional<JejuGudgoCourseSpot> findByJejuGudgoCourseIdOrderByIdDesc(Long courseId);

}

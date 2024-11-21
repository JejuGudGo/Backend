package com.example.jejugudgo.domain.course.jejugudgo.repository;

import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourse;
import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourseOption;
import com.example.jejugudgo.domain.course.jejugudgo.entity.WalkingType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JejuGudgoCourseOptionRepository extends JpaRepository<JejuGudgoCourseOption, Long> {
    List<JejuGudgoCourseOption> findByJejuGudgoCourseId(Long courseId);

    Optional<JejuGudgoCourseOption> findByJejuGudgoCourseAndWalkingType(Long jejuGudgoCourseId, String walkingType);

}

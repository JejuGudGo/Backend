package com.gudgo.jeju.domain.planner.repository;

import com.gudgo.jeju.domain.planner.entity.Planner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlannerRepository extends JpaRepository<Planner, Long> {

    Optional<Planner> findByCourseId(Long courseId);
}
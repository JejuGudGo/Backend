package com.gudgo.jeju.domain.planner.planner.repository;

import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.planner.entity.PlannerTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlannerTagRepository extends JpaRepository<PlannerTag, Long> {
    List<PlannerTag> findByPlanner(Planner planner);
}

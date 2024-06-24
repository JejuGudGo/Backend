package com.gudgo.jeju.domain.planner.repository;

import com.gudgo.jeju.domain.planner.entity.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;



public interface SpotRepository extends JpaRepository<Spot, Long> {
    List<Spot> findByCourseIdOrderByOrderNumberAsc(Long courseId);
}

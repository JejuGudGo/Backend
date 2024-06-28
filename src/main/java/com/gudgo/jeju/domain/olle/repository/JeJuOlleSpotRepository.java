package com.gudgo.jeju.domain.olle.repository;

import com.gudgo.jeju.domain.olle.entity.JeJuOlleSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JeJuOlleSpotRepository extends JpaRepository<JeJuOlleSpot, Long> {
    List<JeJuOlleSpot> findAllByJeJuOlleCourse_id(Long olleCourseId);
}

package com.gudgo.jeju.domain.course.repository;

import com.gudgo.jeju.domain.course.entity.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;



public interface SpotRepository extends JpaRepository<Spot, Long> {
    List<Spot> findByCourseIdOrderByOrderNumberAsc(Long courseId);
}

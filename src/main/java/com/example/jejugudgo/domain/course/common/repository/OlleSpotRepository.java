package com.example.jejugudgo.domain.course.common.repository;

import com.example.jejugudgo.domain.course.common.entity.OlleCourse;
import com.example.jejugudgo.domain.course.common.entity.OlleSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OlleSpotRepository extends JpaRepository<OlleSpot, Long> {
    List<OlleSpot> findByOlleCourse(OlleCourse olleCourse);
}

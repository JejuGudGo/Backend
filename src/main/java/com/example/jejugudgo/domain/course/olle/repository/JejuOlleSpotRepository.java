package com.example.jejugudgo.domain.course.olle.repository;

import com.example.jejugudgo.domain.course.olle.entity.JejuOlleCourse;
import com.example.jejugudgo.domain.course.olle.entity.JejuOlleSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JejuOlleSpotRepository extends JpaRepository<JejuOlleSpot, Long> {
    List<JejuOlleSpot> findByJejuOlleCourse(JejuOlleCourse jejuOlleCourse);
}

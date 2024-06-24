package com.gudgo.jeju.domain.olle.repository;

import com.gudgo.jeju.domain.olle.entity.JeJuOlleCourse;
import com.gudgo.jeju.domain.olle.entity.OlleType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JeJuOlleCourseRepository extends JpaRepository<JeJuOlleCourse, Long> {
    JeJuOlleCourse findByTitle(String name);
    JeJuOlleCourse findByOlleTypeAndCourseNumberAndWheelchairAccessible(OlleType olleType, String courseNumber, boolean wheelchairAccessible);
}

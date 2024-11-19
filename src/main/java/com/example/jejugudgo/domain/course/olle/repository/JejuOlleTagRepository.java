package com.example.jejugudgo.domain.course.olle.repository;

import com.example.jejugudgo.domain.course.olle.entity.JejuOlleCourse;
import com.example.jejugudgo.domain.course.olle.entity.JejuOlleCourseTag;
import com.example.jejugudgo.domain.course.olle.entity.OlleTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JejuOlleTagRepository extends JpaRepository<JejuOlleCourseTag, Long> {
    List<JejuOlleCourseTag> findByJejuOlleCourse(JejuOlleCourse jejuOlleCourse);
    boolean existsByJejuOlleCourseAndOlleTag(JejuOlleCourse course, OlleTag olleTag);
}
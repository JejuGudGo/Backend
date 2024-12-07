package com.example.jejugudgo.domain.course.common.repository;

import com.example.jejugudgo.domain.course.common.entity.OlleCourse;
import com.example.jejugudgo.domain.course.common.entity.OlleCourseTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OlleCourseTagRepository extends JpaRepository<OlleCourseTag, Long> {
    List<OlleCourseTag> findByOlleCourse(OlleCourse olleCourse);
    boolean existsByOlleCourseAndTitle(OlleCourse olleCourse, OlleCourseTag olleCourseTag);
}

package com.example.jejugudgo.domain.course.common.repository;

import com.example.jejugudgo.domain.course.common.entity.OlleCourse;
import com.example.jejugudgo.domain.course.common.entity.OlleCourseLineData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OlleCourseLineDataRepository extends JpaRepository<OlleCourseLineData, Long> {
    List<OlleCourseLineData> findByOlleCourseOrderByDataOrderAsc(OlleCourse olleCourse);
}


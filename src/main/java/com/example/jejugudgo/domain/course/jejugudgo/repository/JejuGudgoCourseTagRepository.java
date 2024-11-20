package com.example.jejugudgo.domain.course.jejugudgo.repository;

import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourse;
import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourseTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JejuGudgoCourseTagRepository extends JpaRepository<JejuGudgoCourseTag, Long> {
    List<JejuGudgoCourseTag> findByJejuGudgoCourse(JejuGudgoCourse jejuGudgoCourse);
}

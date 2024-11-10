package com.example.jejugudgo.domain.olle.repository;

import com.example.jejugudgo.domain.olle.entity.JejuOlleCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JejuOlleCourseRepository extends JpaRepository<JejuOlleCourse, Long> {
    Optional<JejuOlleCourse> findByTitle(String title);
}

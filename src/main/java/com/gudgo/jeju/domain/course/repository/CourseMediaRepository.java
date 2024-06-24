package com.gudgo.jeju.domain.course.repository;

import com.gudgo.jeju.domain.course.entity.CourseMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseMediaRepository extends JpaRepository<CourseMedia, Long> {
}

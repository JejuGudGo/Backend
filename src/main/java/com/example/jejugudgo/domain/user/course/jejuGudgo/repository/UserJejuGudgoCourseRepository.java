package com.example.jejugudgo.domain.user.course.jejuGudgo.repository;

import com.example.jejugudgo.domain.user.course.jejuGudgo.entity.UserJejuGudgoCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserJejuGudgoCourseRepository extends JpaRepository<UserJejuGudgoCourse, Long> {
    List<UserJejuGudgoCourse> findByJejuGudgoCourseId(Long jejuGudgoCourseId);
}

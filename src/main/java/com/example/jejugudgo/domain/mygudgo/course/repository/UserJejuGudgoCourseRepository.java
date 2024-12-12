package com.example.jejugudgo.domain.mygudgo.course.repository;

import com.example.jejugudgo.domain.course.common.entity.JejuGudgoCourse;
import com.example.jejugudgo.domain.mygudgo.course.entity.UserJejuGudgoCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserJejuGudgoCourseRepository extends JpaRepository<UserJejuGudgoCourse, Long> {
    List<UserJejuGudgoCourse> findByJejuGudgoCourse(JejuGudgoCourse jejuGudgoCourse);

    Optional<UserJejuGudgoCourse> findByJejuGudgoCourseAndIsImportedFalse(JejuGudgoCourse jejuGudgoCourse);
}

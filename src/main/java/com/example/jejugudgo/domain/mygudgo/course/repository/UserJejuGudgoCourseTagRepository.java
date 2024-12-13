package com.example.jejugudgo.domain.mygudgo.course.repository;

import com.example.jejugudgo.domain.mygudgo.course.entity.UserJejuGudgoCourse;
import com.example.jejugudgo.domain.mygudgo.course.entity.UserJejuGudgoCourseTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserJejuGudgoCourseTagRepository extends JpaRepository<UserJejuGudgoCourseTag, Long> {
    List<UserJejuGudgoCourseTag> findByUserCourse(UserJejuGudgoCourse userJejuGudgoCourse);
}

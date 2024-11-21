package com.example.jejugudgo.domain.user.course.jejuGudgo.repository;

import com.example.jejugudgo.domain.user.course.jejuGudgo.entity.UserJejuGudgoCourseTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserJejuGudgoCourseTagRepository extends JpaRepository<UserJejuGudgoCourseTag, Long> {
    List<UserJejuGudgoCourseTag> findTagsByUserCourseId(Long userCourseId);
}

package com.example.jejugudgo.domain.user.course.jejuGudgo.repository;

import com.example.jejugudgo.domain.user.course.jejuGudgo.entity.UserJejuGudgoCourseSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserJejuGudgoCourseSpotRepository extends JpaRepository<UserJejuGudgoCourseSpot, Long> {
    List<UserJejuGudgoCourseSpot> findByUserCourseId(Long userCourseId);
}

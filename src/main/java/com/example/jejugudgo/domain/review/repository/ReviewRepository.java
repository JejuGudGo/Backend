package com.example.jejugudgo.domain.review.repository;

import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourse;
import com.example.jejugudgo.domain.course.olle.entity.JejuOlleCourse;
import com.example.jejugudgo.domain.review.entity.Review;
import com.example.jejugudgo.domain.trail.entity.Trail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findDistinctUserByJejuGudgoCourse(JejuGudgoCourse jejuGudgoCourse);
    List<Review> findDistinctUserByJejuOlleCourse(JejuOlleCourse olleCourse);
    List<Review> findDistinctUserByTrail(Trail trail);

    List<Review> findDistinctByJejuGudgoCourse(JejuGudgoCourse jejuGudgoCourse);
    List<Review> findDistinctByJejuOlleCourse(JejuOlleCourse jejuOlleCourse);
    List<Review> findDistinctByTrail(Trail trail);
}

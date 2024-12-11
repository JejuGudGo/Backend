package com.example.jejugudgo.domain.mygudgo.review.repository;

import com.example.jejugudgo.domain.mygudgo.review.entity.UserReview;
import com.example.jejugudgo.domain.mygudgo.review.entity.UserReviewCategory3;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserReviewCategory3Repository extends JpaRepository<UserReviewCategory3, Long> {
    List<UserReviewCategory3> findAllByReview(UserReview review);
}

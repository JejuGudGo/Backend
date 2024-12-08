package com.example.jejugudgo.domain.mygudgo.review.repository;

import com.example.jejugudgo.domain.mygudgo.review.entity.UserReview;
import com.example.jejugudgo.domain.mygudgo.review.entity.UserReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserReviewImageRepository extends JpaRepository<UserReviewImage, Long> {
    List<UserReviewImage> findAllByReview(UserReview review);
}

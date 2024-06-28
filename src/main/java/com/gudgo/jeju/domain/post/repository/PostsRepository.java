package com.gudgo.jeju.domain.post.repository;

import com.gudgo.jeju.domain.post.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostsRepository extends JpaRepository<Posts, Long> {

    Optional<Posts> findByPlannerId(Long plannerId);
}

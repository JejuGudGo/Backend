package com.gudgo.jeju.domain.post.common.repository;

import com.gudgo.jeju.domain.post.common.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostsRepository extends JpaRepository<Posts, Long> {

    Optional<Posts> findByPlannerId(Long plannerId);
}

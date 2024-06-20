package com.gudgo.jeju.domain.post.repository;

import com.gudgo.jeju.domain.post.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Posts, Long> {
}

package com.gudgo.jeju.domain.post.repository;

import com.gudgo.jeju.domain.post.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}

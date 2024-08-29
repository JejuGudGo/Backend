package com.gudgo.jeju.domain.post.column.repository;

import com.gudgo.jeju.domain.post.common.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}

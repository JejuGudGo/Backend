package com.gudgo.jeju.domain.post.column.repository;

import com.gudgo.jeju.domain.post.common.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}

package com.gudgo.jeju.domain.post.repository;

import com.gudgo.jeju.domain.post.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}

package com.example.jejugudgo.domain.course.common.repository;

import com.example.jejugudgo.domain.course.common.entity.Trail;
import com.example.jejugudgo.domain.course.common.enums.TrailTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrailRepository extends JpaRepository<Trail, Long> {
    List<Trail> findByTrailTag(TrailTag trailTag);
}

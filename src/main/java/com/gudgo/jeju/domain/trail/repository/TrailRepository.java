package com.gudgo.jeju.domain.trail.repository;

import com.gudgo.jeju.domain.trail.entity.Trail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrailRepository extends JpaRepository<Trail, Long> {
}

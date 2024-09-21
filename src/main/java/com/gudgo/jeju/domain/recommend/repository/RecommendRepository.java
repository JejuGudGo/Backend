package com.gudgo.jeju.domain.recommend.repository;

import com.gudgo.jeju.domain.recommend.entity.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendRepository  extends JpaRepository<Recommend, Long> {
}

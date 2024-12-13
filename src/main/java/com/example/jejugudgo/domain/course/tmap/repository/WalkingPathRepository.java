package com.example.jejugudgo.domain.course.tmap.repository;

import com.example.jejugudgo.domain.mygudgo.course.entity.UserJejuGudgoSearchOption;
import com.example.jejugudgo.domain.mygudgo.course.enums.SearchOption;
import com.example.jejugudgo.domain.course.tmap.entity.WalkingPath;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalkingPathRepository extends JpaRepository<WalkingPath, Long> {
    Optional<WalkingPath> findByUserJejuGudgoCourseIdAndSearchOption(
            Long userJejuGudgoCourseId,
            UserJejuGudgoSearchOption searchOption
    );
}
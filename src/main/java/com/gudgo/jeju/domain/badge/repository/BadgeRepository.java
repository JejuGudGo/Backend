package com.gudgo.jeju.domain.badge.repository;

import com.gudgo.jeju.domain.badge.entity.Badge;
import com.gudgo.jeju.domain.badge.entity.BadgeCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    List<Badge> findByUserId(Long userId);
    boolean existsByUserIdAndBadgeCode(Long userId, BadgeCode badgeCode);
}

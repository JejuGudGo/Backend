package com.gudgo.jeju.domain.badge.repository;

import com.gudgo.jeju.domain.badge.entity.Badge;
import com.gudgo.jeju.domain.badge.entity.BadgeCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    List<Badge> findByUserId(Long userId);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
            "FROM Badge b WHERE b.user.id = :userId AND b.code = :badgeCode")
    boolean existsByUserIdAndCode(@Param("userId") Long userId, @Param("badgeCode") BadgeCode badgeCode);


}

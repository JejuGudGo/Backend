package com.gudgo.jeju.domain.planner.repository;

import com.gudgo.jeju.domain.planner.entity.MessageLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageLogRepository extends JpaRepository<MessageLog, Long> {
}
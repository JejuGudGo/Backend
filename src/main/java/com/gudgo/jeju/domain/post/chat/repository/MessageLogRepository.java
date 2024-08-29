package com.gudgo.jeju.domain.post.chat.repository;

import com.gudgo.jeju.domain.post.chat.entity.MessageLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageLogRepository extends JpaRepository<MessageLog, Long> {
}
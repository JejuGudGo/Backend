package com.gudgo.jeju.domain.planner.repository;

import com.gudgo.jeju.domain.planner.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}

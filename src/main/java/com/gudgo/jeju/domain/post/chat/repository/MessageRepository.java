package com.gudgo.jeju.domain.post.chat.repository;

import com.gudgo.jeju.domain.post.chat.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}

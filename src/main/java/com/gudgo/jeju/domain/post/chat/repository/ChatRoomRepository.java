package com.gudgo.jeju.domain.post.chat.repository;

import com.gudgo.jeju.domain.post.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}

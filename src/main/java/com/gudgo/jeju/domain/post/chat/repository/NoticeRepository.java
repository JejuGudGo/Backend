package com.gudgo.jeju.domain.post.chat.repository;

import com.gudgo.jeju.domain.post.chat.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}

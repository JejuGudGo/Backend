package com.gudgo.jeju.domain.bookmark.repository;

import com.gudgo.jeju.domain.bookmark.entity.BookMark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookMarkRepository extends JpaRepository<BookMark, Long> {
    List<BookMark> findByUserIdAndIsDeletedFalse(Long userId);
}

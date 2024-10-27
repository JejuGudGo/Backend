package com.example.jejugudgo.domain.bookmark.repository;

import com.example.jejugudgo.domain.bookmark.entity.BookMark;
import com.example.jejugudgo.domain.bookmark.entity.BookMarkType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookMarkRepository extends JpaRepository<BookMark, Long> {

    List<BookMark> findByUserId(Long userId);
    List<BookMark> findByUserIdAndBookMarkType(Long userId, BookMarkType type);
    boolean existsByUserIdAndBookMarkTypeAndTargetId(Long userId, BookMarkType bookMarkType, Long targetId);
}

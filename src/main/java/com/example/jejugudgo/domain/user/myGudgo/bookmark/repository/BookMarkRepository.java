package com.example.jejugudgo.domain.user.myGudgo.bookmark.repository;

import com.example.jejugudgo.domain.user.myGudgo.bookmark.entity.BookMark;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.entity.BookMarkType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookMarkRepository extends JpaRepository<BookMark, Long> {

    List<BookMark> findByUserId(Long userId);
    List<BookMark> findByUserIdAndBookMarkType(Long userId, BookMarkType type);
    boolean existsByUserIdAndBookMarkTypeAndTargetId(Long userId, BookMarkType bookMarkType, Long targetId);
}

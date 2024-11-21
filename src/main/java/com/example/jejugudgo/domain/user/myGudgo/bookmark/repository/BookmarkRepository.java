package com.example.jejugudgo.domain.user.myGudgo.bookmark.repository;

import com.example.jejugudgo.domain.user.myGudgo.bookmark.entity.Bookmark;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.entity.BookmarkType;
import com.example.jejugudgo.domain.user.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findByUserId(Long userId);

    List<Bookmark> findByUserIdAndBookMarkType(Long userId, BookmarkType type);

    boolean existsByUserAndBookMarkTypeAndTargetId(User user, BookmarkType bookMarkType, Long targetId);

    @Query("SELECT DISTINCT b.user.id FROM Bookmark b WHERE b.bookMarkType = :type AND b.targetId = :targetId")
    List<Long> findDistinctUserByBookMarkTypeAndTargetId(@Param("type") BookmarkType type, @Param("targetId") Long targetId);
}

package com.example.jejugudgo.domain.mygudgo.like.repository;

import com.example.jejugudgo.domain.course.common.enums.CourseType;
import com.example.jejugudgo.domain.mygudgo.like.entity.UserLike;
import com.example.jejugudgo.domain.user.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserLikeRepository extends JpaRepository<UserLike, Long> {

    List<UserLike> findByUserId(Long userId);

    List<UserLike> findByUserIdAndBookmarkType(Long userId, CourseType type);

    Optional<UserLike> findByUserAndBookmarkTypeAndTargetId(User user, CourseType bookMarkType, Long targetId);

    @Query("SELECT DISTINCT b.user.id FROM UserLike b WHERE b.bookmarkType = :type AND b.targetId = :targetId")
    List<Long> findDistinctUserByBookMarkTypeAndTargetId(@Param("type") CourseType type, @Param("targetId") Long targetId);
}

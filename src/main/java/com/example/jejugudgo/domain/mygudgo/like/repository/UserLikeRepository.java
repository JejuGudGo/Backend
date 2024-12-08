package com.example.jejugudgo.domain.mygudgo.like.repository;

import com.example.jejugudgo.domain.course.common.enums.CourseType;
import com.example.jejugudgo.domain.mygudgo.like.entity.UserLike;
import com.example.jejugudgo.domain.user.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserLikeRepository extends JpaRepository<UserLike, Long> {
    Page<UserLike> findByUserId(Long userId, Pageable pageable);


    Page<UserLike> findByUserIdAndCourseType(Long userId, CourseType type, Pageable pageable);

    Optional<UserLike> findByUserAndCourseTypeAndTargetId(User user, CourseType bookMarkType, Long targetId);

    @Query("SELECT DISTINCT b.user.id FROM UserLike b WHERE b.courseType = :type AND b.targetId = :targetId")
    List<Long> findDistinctUserByBookMarkTypeAndTargetId(@Param("type") CourseType type, @Param("targetId") Long targetId);
}

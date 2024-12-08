package com.example.jejugudgo.domain.mygudgo.review.entity;

import com.example.jejugudgo.domain.course.common.enums.CourseType;
import com.example.jejugudgo.domain.user.common.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    CourseType reviewType;

    private Long targetId;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    private double star;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;


    @ManyToOne()
    @JoinColumn(name = "userId")
    private User user;


    // TODO : 별점 수정

    // TODO : 수정일자 변경 - 리뷰 다시 확인하기
}

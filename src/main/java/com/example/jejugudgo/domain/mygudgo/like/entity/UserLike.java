package com.example.jejugudgo.domain.mygudgo.like.entity;

import com.example.jejugudgo.domain.course.common.enums.CourseType;
import com.example.jejugudgo.domain.user.common.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Enumerated(value = EnumType.STRING)
    private CourseType bookmarkType;

    private Long targetId;
}

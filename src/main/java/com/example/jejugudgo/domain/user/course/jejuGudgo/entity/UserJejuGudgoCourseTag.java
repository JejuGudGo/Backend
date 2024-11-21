package com.example.jejugudgo.domain.user.course.jejuGudgo.entity;

import com.example.jejugudgo.domain.course.jejugudgo.entity.CourseTag;
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
public class UserJejuGudgoCourseTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private CourseTag courseTag;


    @ManyToOne()
    @JoinColumn(name = "userJejuGudgoCourseId")
    private UserJejuGudgoCourse userJejuGudgoCourse;
}

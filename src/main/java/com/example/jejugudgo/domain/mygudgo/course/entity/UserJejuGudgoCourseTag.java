package com.example.jejugudgo.domain.mygudgo.course.entity;

import com.example.jejugudgo.domain.mygudgo.course.enums.JejuGudgoCourseTag;
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
    private JejuGudgoCourseTag title;


    @ManyToOne()
    @JoinColumn(name = "userCourseId")
    private UserJejuGudgoCourse userCourse;
}

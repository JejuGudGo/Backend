package com.example.jejugudgo.domain.user.course.jejuGudgo.entity;

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
public class UserJejuGudgoRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String selfieImageUrl;

    private String backImageUrl;

    private String content;

    private double latitude;

    private double longitude;


    @ManyToOne()
    @JoinColumn(name = "userCourseId")
    private UserJejuGudgoCourse userCourse;
}

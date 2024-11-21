package com.example.jejugudgo.domain.review.entity;

import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourse;
import com.example.jejugudgo.domain.course.olle.entity.JejuOlleCourse;
import com.example.jejugudgo.domain.trail.entity.Trail;
import com.example.jejugudgo.domain.user.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDate finishedAt;

    private LocalDate createdAt;

    private Long stars;


    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "jejuGudgoCourseId")
    private JejuGudgoCourse jejuGudgoCourse;

    @ManyToOne
    @JoinColumn(name = "jejuOlleCourseId")
    private JejuOlleCourse jejuOlleCourse;

    @ManyToOne
    @JoinColumn(name = "trailId")
    private Trail trail;
}

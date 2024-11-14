package com.example.jejugudgo.domain.user.course.jejuGudgo.entity;

import com.example.jejugudgo.domain.course.entity.JejuGudgoCourse;
import com.example.jejugudgo.domain.user.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserJejuGudgoCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private boolean isPrivate;

    private LocalDate createdAt;

    private LocalDate finishedAt;

    private LocalTime time;

    private double distance;

    private double speed;

    private double kcal;

    private Long steps;

    private String imageUrl;

    private String summary;


    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "jejuGudgoCourseId")
    private JejuGudgoCourse jejuGudgoCourse;
}

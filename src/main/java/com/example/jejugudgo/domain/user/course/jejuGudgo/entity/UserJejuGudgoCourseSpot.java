package com.example.jejugudgo.domain.user.course.jejuGudgo.entity;

import com.example.jejugudgo.domain.course.entity.JejuGudgoCourseSpot;
import com.example.jejugudgo.domain.course.entity.SpotType;
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
public class UserJejuGudgoCourseSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(value = EnumType.STRING)
    private SpotType spotType;

    private Long orderNumber;

    private String address;

    private double latitude;

    private double longitude;

    private boolean isCompleted;


    @ManyToOne()
    @JoinColumn(name = "userCourseId")
    private UserJejuGudgoCourse userCourse;

    @ManyToOne()
    @JoinColumn(name = "jejuGudgoSpotId")
    private JejuGudgoCourseSpot jejuGudgoCourseSpot;
}

package com.example.jejugudgo.domain.user.course.olle.entity;

import com.example.jejugudgo.domain.olle.entity.JejuOlleSpot;
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
public class UserJejuOlleCourseSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isCompleted;


    @ManyToOne
    @JoinColumn(name = "olleSpotId")
    private JejuOlleSpot jejuOlleSpot;

    @ManyToOne
    @JoinColumn(name = "userCourseId")
    private UserJejuOlleCourse userJejuOlleCourse;
}

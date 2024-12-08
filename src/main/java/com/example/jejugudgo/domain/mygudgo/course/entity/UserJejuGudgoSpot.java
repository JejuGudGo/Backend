package com.example.jejugudgo.domain.mygudgo.course.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserJejuGudgoSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private double latitude;

    private double longitude;

    private Long spotOrder;

    private boolean isCompleted;


    @ManyToOne()
    @JoinColumn(name = "userCourseId")
    private UserJejuGudgoCourse userCourse;


    public UserJejuGudgoSpot updateIsCompleted(boolean isCompleted) {
        return toBuilder()
                .isCompleted(isCompleted)
                .build();
    }
}

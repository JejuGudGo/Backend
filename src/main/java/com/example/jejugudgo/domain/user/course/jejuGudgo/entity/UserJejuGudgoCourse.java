package com.example.jejugudgo.domain.user.course.jejuGudgo.entity;

import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourse;
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

    private String time;

    private String distance;

    private double speed;

    private double kcal;

    private Long steps;

    private LocalTime restTime;

    private double averageSpeed;

    private double averagedPace;

    private String imageUrl;

    private String summary;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    private String startSpotTitle;

    private double startLatitude;

    private double startLongitude;

    private String endSpotTitle;

    private double endLatitude;

    private double endLongitude;

    private boolean isCompleted;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "jejuGudgoCourseId")
    private JejuGudgoCourse jejuGudgoCourse;

    public UserJejuGudgoCourse updateCompleted(
            String time, LocalTime restTime,
            String distance, double speed, double kcal,
            Long steps, LocalDate finishedAt,
            double averageSpeed, double averagedPace,
            boolean isCompleted
    ) {
        return UserJejuGudgoCourse.builder()
                .id(id)
                .title(title)
                .isPrivate(isPrivate)
                .createdAt(createdAt)
                .finishedAt(finishedAt)
                .time(time)
                .distance(distance)
                .speed(speed)
                .restTime(restTime)
                .kcal(kcal)
                .steps(steps)
                .averageSpeed(averageSpeed)
                .averagedPace(averagedPace)
                .imageUrl(imageUrl)
                .summary(summary)
                .content(content)
                .startSpotTitle(startSpotTitle)
                .startLatitude(startLatitude)
                .startLongitude(startLongitude)
                .endSpotTitle(endSpotTitle)
                .endLatitude(endLatitude)
                .endLongitude(endLongitude)
                .user(user)
                .jejuGudgoCourse(jejuGudgoCourse)
                .isCompleted(isCompleted)
                .build();
    }
}

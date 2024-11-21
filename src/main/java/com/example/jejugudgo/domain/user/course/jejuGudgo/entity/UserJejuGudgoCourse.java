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

    public UserJejuGudgoCourse updateDetails(String newImageUrl, String newTitle, String newContent) {
        return UserJejuGudgoCourse.builder()
                .id(this.id)
                .title(newTitle != null ? newTitle : this.title)
                .isPrivate(this.isPrivate)
                .createdAt(this.createdAt)
                .finishedAt(this.finishedAt)
                .time(this.time)
                .distance(this.distance)
                .speed(this.speed)
                .restTime(this.restTime)
                .kcal(this.kcal)
                .steps(this.steps)
                .averageSpeed(this.averageSpeed)
                .averagedPace(this.averagedPace)
                .imageUrl(newImageUrl != null ? newImageUrl : this.imageUrl)
                .summary(this.summary)
                .content(newContent != null ? newContent : this.content)
                .startSpotTitle(this.startSpotTitle)
                .startLatitude(this.startLatitude)
                .startLongitude(this.startLongitude)
                .endSpotTitle(this.endSpotTitle)
                .endLatitude(this.endLatitude)
                .endLongitude(this.endLongitude)
                .user(this.user)
                .jejuGudgoCourse(this.jejuGudgoCourse)
                .isCompleted(this.isCompleted)
                .build();
    }
}

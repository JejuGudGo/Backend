package com.gudgo.jeju.domain.olle.entity;

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
public class JeJuOlleCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private OlleType olleType;

    private String courseNumber;

    private String title;

    private double startLatitude;

    private double startLongitude;

    private double endLatitude;

    private double endLongitude;

    private boolean wheelchairAccessible;

    private String totalDistance;

    private String totalTime;


    public JeJuOlleCourse withTotalDistanceAndTotalTime(String totalDistance, String totalTime) {
        return JeJuOlleCourse.builder()
                .id(this.id)
                .olleType(this.olleType)
                .courseNumber(this.courseNumber)
                .title(this.title)
                .startLatitude(this.startLatitude)
                .startLongitude(this.startLongitude)
                .endLatitude(this.endLatitude)
                .endLongitude(this.endLongitude)
                .wheelchairAccessible(this.wheelchairAccessible)
                .totalDistance(totalDistance)
                .totalTime(totalTime)
                .build();
    }
}
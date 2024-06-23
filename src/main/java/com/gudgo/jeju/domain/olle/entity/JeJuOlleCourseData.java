package com.gudgo.jeju.domain.olle.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JeJuOlleCourseData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Long orderNumber;

    private double latitude;

    private double longitude;

    private double altitude;

    private OffsetDateTime time;

    private LocalDate updatedAt;


    @ManyToOne
    @JoinColumn(name = "jeJuOlleCourseId")
    private JeJuOlleCourse jeJuOlleCourse;


    public JeJuOlleCourseData withUpdatedAt(LocalDate updatedAt) {
        return JeJuOlleCourseData.builder()
                .id(this.id)
                .orderNumber(this.orderNumber)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .altitude(this.altitude)
                .time(this.time)
                .updatedAt(updatedAt != null ? updatedAt : this.updatedAt)
                .jeJuOlleCourse(this.jeJuOlleCourse)
                .build();
    }

    public JeJuOlleCourseData withJeJuOlleCourse(JeJuOlleCourse jeJuOlleCourse) {
        return JeJuOlleCourseData.builder()
                .id(this.id)
                .orderNumber(this.orderNumber)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .altitude(this.altitude)
                .time(this.time)
                .updatedAt(this.updatedAt)
                .jeJuOlleCourse(jeJuOlleCourse != null ? jeJuOlleCourse : this.jeJuOlleCourse)
                .build();
    }
}
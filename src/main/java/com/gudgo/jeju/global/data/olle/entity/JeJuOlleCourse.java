package com.gudgo.jeju.global.data.olle.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private String courseNumber;

    private String title;

    private double startLatitude;

    private double startLongitude;

    private double endLatitude;

    private double endLongitude;

    private boolean wheelchairAccessible;
}

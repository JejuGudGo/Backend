package com.example.jejugudgo.domain.course.entity;

import com.example.jejugudgo.domain.course.type.OlleType;
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
public class JejuOlleCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseNumber;

    private String title;

    private double startLatitude;

    private double startLongitude;

    private double endLatitude;

    private double endLongitude;

    private boolean wheelchairAccessible;

    private String totalDistance;

    private String totalTime;

    private double starAvg;

    @Enumerated(value = EnumType.STRING)
    private OlleType olleType;

}

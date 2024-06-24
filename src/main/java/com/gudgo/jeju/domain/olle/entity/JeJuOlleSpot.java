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
public class JeJuOlleSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private double latitude;

    private double longitude;

    private String distance;

    private Long orderNumber;


    @ManyToOne
    @JoinColumn(name = "jeJuOlleCourseId")
    private JeJuOlleCourse jeJuOlleCourse;
}

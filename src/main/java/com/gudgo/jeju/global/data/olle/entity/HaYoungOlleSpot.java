package com.gudgo.jeju.global.data.olle.entity;

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
public class HaYoungOlleSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private double latitude;

    private double longitude;

    @ManyToOne
    @JoinColumn(name = "jeJuOlleCourseId")
    private JeJuOlleCourse jeJuOlleCourse;
}

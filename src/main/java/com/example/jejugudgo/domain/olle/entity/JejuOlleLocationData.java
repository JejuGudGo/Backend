package com.example.jejugudgo.domain.olle.entity;

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
public class JejuOlleLocationData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long locationOrder;

    private double latitude;

    private double longitude;

    private OffsetDateTime time;

    private LocalDate updatedAt;

    @ManyToOne
    @JoinColumn(name = "jejuOlleCourseId")
    private JejuOlleCourse jejuOlleCourse;
}

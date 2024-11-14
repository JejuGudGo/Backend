package com.example.jejugudgo.domain.course.olle.entity;

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
public class JejuOlleSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private double latitude;

    private double longitude;

    private Long spotOrder;

    @ManyToOne
    @JoinColumn(name = "jejuOlleCourseId")
    private JejuOlleCourse jejuOlleCourse;
}

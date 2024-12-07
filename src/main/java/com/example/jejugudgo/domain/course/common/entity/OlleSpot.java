package com.example.jejugudgo.domain.course.common.entity;

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
public class OlleSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private double latitude;

    private double longitude;

    private Long spotOrder;


    @ManyToOne
    @JoinColumn(name = "olleCourseId")
    private OlleCourse olleCourse;
}

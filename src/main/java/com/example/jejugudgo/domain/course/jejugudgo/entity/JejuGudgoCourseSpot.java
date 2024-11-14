package com.example.jejugudgo.domain.course.jejugudgo.entity;

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
public class JejuGudgoCourseSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(value = EnumType.STRING)
    private SpotType spotType;

    private Long orderNumber;

    private String address;

    private double latitude;

    private double longitude;

    @ManyToOne
    @JoinColumn(name = "courseId")
    private JejuGudgoCourse jejuGudgoCourse;
}

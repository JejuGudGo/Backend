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
public class OlleCourseLineData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long dataOrder;

    private double latitude;

    private double longitude;


    @ManyToOne
    @JoinColumn(name = "olleCourseId")
    private OlleCourse olleCourse;
}

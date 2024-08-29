package com.gudgo.jeju.domain.planner.course.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private CourseType type;

    private String title;

    private LocalDate createdAt;

    private Long originalCreatorId;

    private Long originalCourseId;

    private Long olleCourseId;

    private double starAvg;

    public Course withTitle(String title) {
        return Course.builder()
                .id(id)
                .title(title)
                .type(type)
                .createdAt(createdAt)
                .originalCourseId(originalCourseId)
                .originalCreatorId(originalCreatorId)
                .olleCourseId(olleCourseId)
                .starAvg(starAvg)
                .build();
    }

    public Course withOriginalCourseId(Long courseId) {
        return Course.builder()
                .id(id)
                .title(title)
                .type(type)
                .createdAt(createdAt)
                .originalCourseId(originalCourseId)
                .originalCreatorId(originalCreatorId)
                .olleCourseId(olleCourseId)
                .starAvg(starAvg)
                .build();
    }

    public Course withOlleCourseId(Long olleCourseId) {
        return Course.builder()
                .id(id)
                .title(title)
                .type(type)
                .createdAt(createdAt)
                .originalCourseId(originalCourseId)
                .originalCreatorId(originalCreatorId)
                .olleCourseId(olleCourseId)
                .starAvg(starAvg)
                .build();
    }



}
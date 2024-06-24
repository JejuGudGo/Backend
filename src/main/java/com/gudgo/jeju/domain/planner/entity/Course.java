package com.gudgo.jeju.domain.planner.entity;

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

    @OneToOne
    @JoinColumn(name = "plannerId")
    private Planner planner;

    public Course withTitle(String title) {
        return Course.builder()
                .id(this.id)
                .title(title)
                .type(this.type)
                .createdAt(this.createdAt)
                .originalCourseId(this.originalCourseId)
                .originalCreatorId(this.originalCreatorId)
                .olleCourseId(this.olleCourseId)
                .planner(this.planner)
                .build();
    }

    public Course withOriginalCourseId(Long courseId) {
        return Course.builder()
                .id(this.id)
                .title(title)
                .type(this.type)
                .createdAt(this.createdAt)
                .originalCourseId(this.originalCourseId)
                .originalCreatorId(this.originalCreatorId)
                .olleCourseId(this.olleCourseId)
                .planner(this.planner)
                .build();
    }



}
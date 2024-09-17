package com.gudgo.jeju.domain.planner.course.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;


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

    private LocalTime timeLabs;

    private String imageUrl;

    private String content;

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
                .timeLabs(timeLabs)
                .imageUrl(imageUrl)
                .content(content)
                .build();
    }

    public Course withOriginalCourseId(Long originalCourseId) {
        return Course.builder()
                .id(id)
                .title(title)
                .type(type)
                .createdAt(createdAt)
                .originalCourseId(originalCourseId)
                .originalCreatorId(originalCreatorId)
                .olleCourseId(olleCourseId)
                .starAvg(starAvg)
                .imageUrl(imageUrl)
                .content(content)
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
                .imageUrl(imageUrl)
                .content(content)
                .build();
    }

    public Course withStarAvg(double starAvg) {
        return Course.builder()
                .id(id)
                .title(title)
                .type(type)
                .createdAt(createdAt)
                .originalCourseId(originalCourseId)
                .originalCreatorId(originalCreatorId)
                .olleCourseId(olleCourseId)
                .starAvg(starAvg)
                .imageUrl(imageUrl)
                .content(content)
                .build();
    }

    public Course withImageUrl(String imageUrl) {
        return Course.builder()
                .id(id)
                .title(title)
                .type(type)
                .createdAt(createdAt)
                .originalCourseId(originalCourseId)
                .originalCreatorId(originalCreatorId)
                .olleCourseId(olleCourseId)
                .starAvg(starAvg)
                .imageUrl(imageUrl)
                .content(content)
                .build();
    }


    public Course withContent(String content) {
        return Course.builder()
                .id(id)
                .title(title)
                .type(type)
                .createdAt(createdAt)
                .originalCourseId(originalCourseId)
                .originalCreatorId(originalCreatorId)
                .olleCourseId(olleCourseId)
                .starAvg(starAvg)
                .imageUrl(imageUrl)
                .content(content)
                .build();
    }

    public Course withTimeLabs(LocalTime timeLabs) {
        return Course.builder()
                .id(id)
                .title(title)
                .type(type)
                .createdAt(createdAt)
                .originalCourseId(originalCourseId)
                .originalCreatorId(originalCreatorId)
                .olleCourseId(olleCourseId)
                .starAvg(starAvg)
                .imageUrl(imageUrl)
                .content(content)
                .timeLabs(timeLabs)
                .build();
    }
}
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

    private String totalDistance;

    private LocalDate createdAt;

    private Long originalCreatorId;

    private Long originalCourseId;

    private Long olleCourseId;

    private double starAvg;

    private double startLatitude;

    private double startLongitude;

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
                .startLatitude(startLatitude)
                .startLongitude(startLongitude)
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
                .startLatitude(startLatitude)
                .startLongitude(startLongitude)
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
                .startLatitude(startLatitude)
                .startLongitude(startLongitude)
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
                .startLatitude(startLatitude)
                .startLongitude(startLongitude)
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
                .startLatitude(startLatitude)
                .startLongitude(startLongitude)
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
                .startLatitude(startLatitude)
                .startLongitude(startLongitude)
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
                .startLatitude(startLatitude)
                .startLongitude(startLongitude)
                .imageUrl(imageUrl)
                .content(content)
                .timeLabs(timeLabs)
                .build();
    }

    public Course withStartPoint(double startLatitude, double startLongitude) {
        return Course.builder()
                .id(id)
                .title(title)
                .type(type)
                .createdAt(createdAt)
                .originalCourseId(originalCourseId)
                .originalCreatorId(originalCreatorId)
                .olleCourseId(olleCourseId)
                .starAvg(starAvg)
                .startLatitude(startLatitude)
                .startLongitude(startLongitude)
                .imageUrl(imageUrl)
                .content(content)
                .timeLabs(timeLabs)
                .build();
    }
}
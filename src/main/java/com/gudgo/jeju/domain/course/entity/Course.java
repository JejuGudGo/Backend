package com.gudgo.jeju.domain.course.entity;

import com.gudgo.jeju.domain.user.entity.User;
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

    private LocalTime time;

    private LocalDate startAt;

    private String summary;

    private LocalDate createdAt;

    private boolean isCompleted = false;

    private boolean isDeleted = false;

    private Long originalCreatorId;

    private Long originalCourseId;

    private Long olleCourseId;

    private Long postId;


    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;


    public Course withTitle(String title) {
        return Course.builder()
                .id(this.id)
                .title(title)
                .time(this.time)
                .startAt(this.startAt)
                .summary(this.summary)
                .createdAt(this.createdAt)
                .isCompleted(true)
                .isDeleted(this.isDeleted)
                .originalCourseId(this.originalCourseId)
                .originalCreatorId(this.originalCreatorId)
                .olleCourseId(this.olleCourseId)
                .user(this.user)
                .postId(this.postId)
                .build();
    }

    public Course withIsCompleted() {
        return Course.builder()
                .id(this.id)
                .title(this.title)
                .time(this.time)
                .startAt(this.startAt)
                .summary(this.summary)
                .createdAt(this.createdAt)
                .isCompleted(true)
                .isDeleted(this.isDeleted)
                .originalCourseId(this.originalCourseId)
                .olleCourseId(this.olleCourseId)
                .originalCreatorId(this.originalCreatorId)
                .user(this.user)
                .postId(this.postId)
                .build();
    }

    public Course withOriginalCourseId(Long courseId) {
        return Course.builder()
                .id(this.id)
                .title(this.title)
                .time(this.time)
                .startAt(this.startAt)
                .summary(this.summary)
                .createdAt(this.createdAt)
                .isCompleted(this.isCompleted)
                .isDeleted(this.isDeleted)
                .originalCourseId(courseId)
                .originalCreatorId(this.originalCreatorId)
                .olleCourseId(this.olleCourseId)
                .user(this.user)
                .postId(this.postId)
                .build();
    }

    public Course withStartAt(LocalDate startAt) {
        return Course.builder()
                .id(this.id)
                .title(this.title)
                .time(this.time)
                .startAt(startAt)
                .summary(this.summary)
                .createdAt(this.createdAt)
                .isCompleted(this.isCompleted)
                .isDeleted(this.isDeleted)
                .originalCourseId(this.originalCourseId)
                .originalCreatorId(this.originalCreatorId)
                .olleCourseId(this.olleCourseId)
                .user(this.user)
                .postId(this.postId)
                .build();
    }

    public Course withDeleted() {
        return Course.builder()
                .id(this.id)
                .title(this.title)
                .time(this.time)
                .startAt(this.startAt)
                .summary(this.summary)
                .createdAt(this.createdAt)
                .isCompleted(this.isCompleted)
                .isDeleted(true)
                .originalCourseId(this.originalCourseId)
                .originalCreatorId(this.originalCreatorId)
                .olleCourseId(this.olleCourseId)
                .user(this.user)
                .postId(this.postId)
                .build();
    }

    public Course withPostId(Long postId) {
        return Course.builder()
                .id(this.id)
                .title(this.title)
                .time(this.time)
                .startAt(this.startAt)
                .summary(this.summary)
                .createdAt(this.createdAt)
                .isCompleted(this.isCompleted)
                .isDeleted(true)
                .originalCourseId(this.originalCourseId)
                .originalCreatorId(this.originalCreatorId)
                .olleCourseId(this.olleCourseId)
                .user(this.user)
                .postId(postId)
                .build();
    }


}
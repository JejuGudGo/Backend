package com.gudgo.jeju.domain.course.entity;

import com.gudgo.jeju.domain.post.entity.Posts;
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
                .createdAt(this.createdAt)
                .originalCourseId(this.originalCourseId)
                .originalCreatorId(this.originalCreatorId)
                .olleCourseId(this.olleCourseId)
                .build();
    }

    public Course withIsCompleted() {
        return Course.builder()
                .id(this.id)
                .title(this.title)
                .createdAt(this.createdAt)
                .originalCourseId(this.originalCourseId)
                .olleCourseId(this.olleCourseId)
                .originalCreatorId(this.originalCreatorId)
                .build();
    }

    public Course withOriginalCourseId(Long courseId) {
        return Course.builder()
                .id(this.id)
                .title(this.title)
                .createdAt(this.createdAt)
                .originalCourseId(courseId)
                .originalCreatorId(this.originalCreatorId)
                .olleCourseId(this.olleCourseId)
                .build();
    }

    public Course withStartAt(LocalDate startAt) {
        return Course.builder()
                .id(this.id)
                .title(this.title)
                .createdAt(this.createdAt)
                .originalCourseId(this.originalCourseId)
                .originalCreatorId(this.originalCreatorId)
                .olleCourseId(this.olleCourseId)
                .build();
    }

    public Course withDeleted() {
        return Course.builder()
                .id(this.id)
                .title(this.title)
                .createdAt(this.createdAt)
                .originalCourseId(this.originalCourseId)
                .originalCreatorId(this.originalCreatorId)
                .olleCourseId(this.olleCourseId)
                .build();
    }

    public Course withPost(Posts post) {
        return Course.builder()
                .id(this.id)
                .title(this.title)
                .originalCourseId(this.originalCourseId)
                .originalCreatorId(this.originalCreatorId)
                .olleCourseId(this.olleCourseId)
                .build();
    }


}
package com.gudgo.jeju.domain.course.entity;

import com.gudgo.jeju.domain.course.dto.request.plan.PlanUpdateStartRequestDto;
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

    private String title;

    private LocalTime time;

    private LocalDate startAt;

    private String summary;

    private LocalDate createdAt;

    private boolean isCompleted = false;

    private boolean isDeleted = false;

    private Long originalCreatorId;

    private Long originalCourseId;


    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

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
                .originalCreatorId(this.originalCreatorId)
                .user(this.user)
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
                .user(this.user)
                .build();
    }

    public Course withStartAt(PlanUpdateStartRequestDto requestDto) {
        return Course.builder()
                .id(this.id)
                .title(this.title)
                .time(this.time)
                .startAt(requestDto.getStartAt())
                .summary(this.summary)
                .createdAt(this.createdAt)
                .isCompleted(this.isCompleted)
                .isDeleted(this.isDeleted)
                .originalCourseId(this.originalCourseId)
                .originalCreatorId(this.originalCreatorId)
                .user(this.user)
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
                .user(this.user)
                .build();
    }


}

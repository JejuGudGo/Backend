package com.gudgo.jeju.domain.course.entity;

import com.gudgo.jeju.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public void updateOriginalCourseId(Long id) {
        this.originalCourseId = id;
    }

    public void softDelete(boolean isDeleted) { this.isDeleted = true; }





}

package com.gudgo.jeju.domain.planner.entity;

import com.gudgo.jeju.domain.planner.repository.PlannerRepository;
import com.gudgo.jeju.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Planner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startAt;

    private boolean isDeleted;

    private boolean isPrivate;

    private String summary;

    private LocalTime time;

    private boolean isCompleted;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @OneToOne
    @JoinColumn(name = "courseId")
    private Course course;

    public Planner withCompleted(boolean isCompleted) {
        return Planner.builder()
                .id(id)
                .startAt(startAt)
                .isDeleted(isDeleted)
                .isPrivate(isPrivate)
                .summary(summary)
                .time(time)
                .isCompleted(isCompleted)
                .user(user)
                .course(course)
                .build();
    }
    public Planner withDeleted(boolean isDeleted) {
        return Planner.builder()
                .id(id)
                .startAt(startAt)
                .isDeleted(isDeleted)
                .isPrivate(isPrivate)
                .summary(summary)
                .time(time)
                .isCompleted(isCompleted)
                .user(user)
                .course(course)
                .build();
    }
}

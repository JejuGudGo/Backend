package com.gudgo.jeju.domain.course.entity;

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

    private Long summary;

    private LocalTime time;

    private boolean isCompleted;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;


}

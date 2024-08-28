package com.gudgo.jeju.domain.planner.entity;

import com.gudgo.jeju.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean approved;

    private boolean isDeleted;

    private Long count;

    private boolean isApplied;

    private LocalDate appliedAt;

    private LocalDate approvedAt;

    private boolean isBlocked;

    private String content;


    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name="plannerId")
    private Planner planner;


    public Participant withApplied(boolean isApplied) {
        return Participant.builder()
                .id(id)
                .approved(approved)
                .isDeleted(isDeleted)
                .count(count)
                .isApplied(isApplied)
                .user(user)
                .planner(planner)
                .appliedAt(appliedAt)
                .approvedAt(approvedAt)
                .isBlocked(isBlocked)
                .content(content)
                .build();
    }

    public Participant withCountAndIsAppliedAndAppliedAt(boolean isApplied, LocalDate appliedAt) {
        return Participant.builder()
                .id(id)
                .approved(approved)
                .isDeleted(isDeleted)
                .count(count+1)
                .isApplied(isApplied)
                .user(user)
                .planner(planner)
                .appliedAt(appliedAt)
                .approvedAt(approvedAt)
                .isBlocked(isBlocked)
                .content(content)
                .build();
    }

    public Participant withApprovedAndApprovedAt(boolean approved, LocalDate approvedAt) {
        return Participant.builder()
                .id(id)
                .approved(approved)
                .isDeleted(isDeleted)
                .count(count)
                .isApplied(isApplied)
                .user(user)
                .planner(planner)
                .appliedAt(appliedAt)
                .approvedAt(approvedAt)
                .isBlocked(isBlocked)
                .content(content)
                .build();
    }
}

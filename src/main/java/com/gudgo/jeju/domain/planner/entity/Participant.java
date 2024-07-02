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


    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name="plannerId")
    private Planner planner;

//    public Participant withCountAndApplied(boolean isApplied) {
//        return Participant.builder()
//                .id(this.id)
//                .approved(this.approved)
//                .isDeleted(this.isDeleted)
//                .count(this.count++)
//                .isApplied(isApplied)
//                .user(this.user)
//                .planner(this.planner)
//                .appliedAt(this.appliedAt)
//                .approvedAt(this.approvedAt)
//                .build();
//    }

    public Participant withApplied(boolean isApplied) {
        return Participant.builder()
                .id(this.id)
                .approved(this.approved)
                .isDeleted(this.isDeleted)
                .count(this.count)
                .isApplied(isApplied)
                .user(this.user)
                .planner(this.planner)
                .appliedAt(this.appliedAt)
                .approvedAt(this.approvedAt)
                .build();
    }

//    public Participant withApproved(boolean approved) {
//        return Participant.builder()
//                .id(this.id)
//                .approved(approved)
//                .isDeleted(this.isDeleted)
//                .count(this.count)
//                .isApplied(this.isApplied)
//                .user(this.user)
//                .planner(this.planner)
//                .appliedAt(this.appliedAt)
//                .approvedAt(this.approvedAt)
//                .build();
//    }

//    public Participant withAppliedAt(LocalDate appliedAt) {
//        return Participant.builder()
//                .id(this.id)
//                .approved(this.approved)
//                .isDeleted(this.isDeleted)
//                .count(this.count)
//                .isApplied(this.isApplied)
//                .user(this.user)
//                .planner(this.planner)
//                .appliedAt(appliedAt)
//                .approvedAt(this.approvedAt)
//                .build();
//    }

    public Participant withCountAndIsAppliedAndAppliedAt(boolean isApplied, LocalDate appliedAt) {
        return Participant.builder()
                .id(this.id)
                .approved(this.approved)
                .isDeleted(this.isDeleted)
                .count(this.count++)
                .isApplied(isApplied)
                .user(this.user)
                .planner(this.planner)
                .appliedAt(appliedAt)
                .approvedAt(this.approvedAt)
                .build();
    }

    public Participant withApprovedAndApprovedAt(boolean approved, LocalDate approvedAt) {
        return Participant.builder()
                .id(this.id)
                .approved(approved)
                .isDeleted(this.isDeleted)
                .count(this.count)
                .isApplied(this.isApplied)
                .user(this.user)
                .planner(this.planner)
                .appliedAt(this.appliedAt)
                .approvedAt(approvedAt)
                .build();
    }
}

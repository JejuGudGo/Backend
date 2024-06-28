package com.gudgo.jeju.domain.planner.entity;

import com.gudgo.jeju.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name="plannerId")
    private Planner planner;

    public Participant withCountAndApplied(boolean isApplied) {
        return Participant.builder()
                .id(this.id)
                .approved(this.approved)
                .isDeleted(this.isDeleted)
                .count(this.count++)
                .isApplied(isApplied)
                .user(this.user)
                .planner(this.planner)
                .build();
    }

    public Participant withApplied(boolean isApplied) {
        return Participant.builder()
                .id(this.id)
                .approved(this.approved)
                .isDeleted(this.isDeleted)
                .count(this.count)
                .isApplied(isApplied)
                .user(this.user)
                .planner(this.planner)
                .build();
    }

    public Participant withApproved(boolean approved) {
        return Participant.builder()
                .id(this.id)
                .approved(approved)
                .isDeleted(this.isDeleted)
                .count(this.count)
                .isApplied(this.isApplied)
                .user(this.user)
                .planner(this.planner)
                .build();
    }
}

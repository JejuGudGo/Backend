package com.gudgo.jeju.domain.course.entity;

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

    private Long participantUserId;

    private boolean approved;

    private boolean isDeleted;

    private Long count;

    private boolean isApplied;

    @ManyToOne
    @JoinColumn(name = "courseId")
    private Course course;

    public Participant withCountAndApplied(boolean isApplied) {
        return Participant.builder()
                .id(this.id)
                .participantUserId(this.participantUserId)
                .approved(this.approved)
                .isDeleted(this.isDeleted)
                .count(this.count++)
                .isApplied(isApplied)
                .course(course)
                .build();
    }

    public Participant withApplied(boolean isApplied) {
        return Participant.builder()
                .id(this.id)
                .participantUserId(this.participantUserId)
                .approved(this.approved)
                .isDeleted(this.isDeleted)
                .count(this.count)
                .isApplied(isApplied)
                .course(course)
                .build();
    }

    public Participant withApproved(boolean approved) {
        return Participant.builder()
                .id(this.id)
                .participantUserId(this.participantUserId)
                .approved(approved)
                .isDeleted(this.isDeleted)
                .count(this.count)
                .isApplied(this.isApplied)
                .course(course)
                .build();
    }

}

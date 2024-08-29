package com.gudgo.jeju.domain.planner.review.entity;


import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlannerReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private LocalDate createdAt;

    private boolean isDeleted;

    private Long stars;

    @ManyToOne
    @JoinColumn(name = "plannerId")
    private Planner planner;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;


    public PlannerReview withIsDeleted(boolean isDeleted) {
        return PlannerReview.builder()
                .id(id)
                .content(content)
                .createdAt(createdAt)
                .isDeleted(isDeleted)
                .planner(planner)
                .user(user)
                .stars(stars)
                .build();
    }

    public PlannerReview withContent(String content) {
        return PlannerReview.builder()
                .id(id)
                .content(content)
                .createdAt(createdAt)
                .isDeleted(isDeleted)
                .planner(planner)
                .user(user)
                .stars(stars)
                .build();
    }

    public PlannerReview withStars(Long stars) {
        return PlannerReview.builder()
                .id(id)
                .content(content)
                .createdAt(createdAt)
                .isDeleted(isDeleted)
                .planner(planner)
                .user(user)
                .stars(stars)
                .build();
    }
}

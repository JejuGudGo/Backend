package com.gudgo.jeju.global.data.review.entity;


import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.trail.entity.Trail;
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
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private LocalDate createdAt;

    private boolean isDeleted;

    private double stars;

    @Enumerated(EnumType.STRING)
    private ReviewCategory reviewCategory;

    @ManyToOne
    @JoinColumn(name = "plannerId")
    private Planner planner;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "trail")
    private Trail trail;


    public Review withIsDeleted(boolean isDeleted) {
        return Review.builder()
                .id(this.id)
                .content(this.content)
                .createdAt(this.createdAt)
                .isDeleted(isDeleted)
                .planner(this.planner)
                .user(this.user)
                .trail(this.trail)
                .stars(this.stars)
                .build();
    }

    public Review withContent(String content) {
        return Review.builder()
                .id(this.id)
                .content(content)
                .createdAt(this.createdAt)
                .isDeleted(isDeleted)
                .planner(this.planner)
                .user(this.user)
                .trail(this.trail)
                .stars(this.stars)
                .build();
    }

    public Review withStars(Long stars) {
        return Review.builder()
                .id(this.id)
                .content(this.content)
                .createdAt(this.createdAt)
                .isDeleted(isDeleted)
                .planner(this.planner)
                .user(this.user)
                .trail(this.trail)
                .stars(stars)
                .build();
    }
}

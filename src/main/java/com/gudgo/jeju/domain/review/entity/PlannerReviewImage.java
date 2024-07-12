package com.gudgo.jeju.domain.review.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlannerReviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "plannerReviewId")
    private PlannerReview plannerReview;

    public PlannerReviewImage withIsDeleted(boolean isDeleted) {
        return PlannerReviewImage.builder()
                .id(id)
                .imageUrl(imageUrl)
                .isDeleted(isDeleted)
                .plannerReview(plannerReview)
                .build();
    }
}

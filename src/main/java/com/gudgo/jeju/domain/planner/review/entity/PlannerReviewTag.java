package com.gudgo.jeju.domain.planner.review.entity;

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
public class PlannerReviewTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

//    private String title;

    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "plannerReviewCategoryId")
    private PlannerReviewCategory plannerReviewCategory;

    public PlannerReviewTag withIsDeleted(boolean isDeleted) {
        return PlannerReviewTag.builder()
                .id(id)
                .code(code)
                .isDeleted(isDeleted)
                .plannerReviewCategory(plannerReviewCategory)
                .build();
    }
}

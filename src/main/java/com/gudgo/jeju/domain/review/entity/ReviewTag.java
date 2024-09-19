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
public class ReviewTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReviewFilterTag filterTag;

    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "reviewId")
    private Review review;


    public ReviewTag withIsDeleted() {
        return ReviewTag.builder()
                .id(this.id)
                .filterTag(this.filterTag)
                .isDeleted(true)
                .review(this.review)
                .build();
    }
}

package com.gudgo.jeju.global.data.review.entity;

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
public class ReviewPurposeTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PurposeType purposeType;

    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "reviewId")
    private Review  review;

    public ReviewPurposeTag withIsDeleted() {
        return ReviewPurposeTag.builder()
                .id(this.id)
                .purposeType(this.purposeType)
                .isDeleted(true)
                .review(this.review)
                .build();
    }
}

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
public class ReviewTogetherTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TogetherType togetherType;

    private boolean isDeleted;


    @ManyToOne
    @JoinColumn(name = "reviewId")
    private Review  review;


    public ReviewTogetherTag withIsDeleted() {
        return ReviewTogetherTag.builder()
                .id(this.id)
                .togetherType(this.togetherType)
                .isDeleted(true)
                .review(this.review)
                .build();
    }
}

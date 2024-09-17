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
public class ReviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "reviewId")
    private Review review;

    public ReviewImage withIsDeleted(boolean isDeleted) {
        return ReviewImage.builder()
                .id(id)
                .imageUrl(imageUrl)
                .isDeleted(isDeleted)
                .review(review)
                .build();
    }
}

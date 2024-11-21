package com.example.jejugudgo.domain.review.entity;

import com.example.jejugudgo.domain.review.enums.ReviewCategory1;
import com.example.jejugudgo.domain.review.enums.ReviewCategory2;
import com.example.jejugudgo.domain.review.enums.ReviewCategory3;
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
public class ReviewCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReviewCategory1 category1;

    @Enumerated(EnumType.STRING)
    private ReviewCategory2 category2;

    @Enumerated(EnumType.STRING)
    private ReviewCategory3 category3;


    @ManyToOne
    @JoinColumn(name = "reviewId")
    private Review review;
}

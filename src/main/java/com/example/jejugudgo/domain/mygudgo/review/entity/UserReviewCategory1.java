package com.example.jejugudgo.domain.mygudgo.review.entity;

import com.example.jejugudgo.domain.mygudgo.review.enums.Category1Type;
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
public class UserReviewCategory1 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Category1Type title;


    @ManyToOne
    @JoinColumn(name = "reviewId")
    private UserReview review;
}
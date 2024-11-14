package com.example.jejugudgo.domain.review.enums;

import lombok.Getter;

@Getter
public enum ReviewCategory2 {
    REVIEW_CAT0201("연인, 배우자"),
    REVIEW_CAT0202("친구"),
    REVIEW_CAT0203("혼자"),
    REVIEW_CAT0204("가족"),
    REVIEW_CAT0205("반려동물"),
    REVIEW_CAT0206("직장동료"),
    REVIEW_CAT0207("제주걷고 동행");

    private final String category2;

    ReviewCategory2(String category2) {
        this.category2 = category2;
    }

    public ReviewCategory2 fromQuery(String query) {
        for (ReviewCategory2 category : ReviewCategory2.values()) {
            if (category.getCategory2().equals(query)) {
                return category;
            }
        }

        return null;
    }
}

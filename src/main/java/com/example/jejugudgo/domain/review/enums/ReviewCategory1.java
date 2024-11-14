package com.example.jejugudgo.domain.review.enums;

import lombok.Getter;

@Getter
public enum ReviewCategory1 {
    REVIEW_CAT0101("체력 증진"),
    REVIEW_CAT0102("자연 감상"),
    REVIEW_CAT0103("도전・성취"),
    REVIEW_CAT0104("주변 관광"),
    REVIEW_CAT0105("사회적 교류"),
    REVIEW_CAT0106("스트레스 해소"),
    REVIEW_CAT0107("지인과의 시간");

    private final String category1;

    ReviewCategory1(String category1) {
        this.category1 = category1;
    }

    public ReviewCategory1 fromQuery(String query) {
        for (ReviewCategory1 category : ReviewCategory1.values()) {
            if (category.getCategory1().equals(query)) {
                return category;
            }
        }

        return null;
    }
}

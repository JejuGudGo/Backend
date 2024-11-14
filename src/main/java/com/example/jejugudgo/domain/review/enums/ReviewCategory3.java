package com.example.jejugudgo.domain.review.enums;

import lombok.Getter;

@Getter
public enum ReviewCategory3 {
    REVIEW_CAT0301("아름다운 경치"),
    REVIEW_CAT0302("안전한 길"),
    REVIEW_CAT0303("평온한 분위기"),
    REVIEW_CAT0304("많은 편의시설"),
    REVIEW_CAT0305("도전적"),
    REVIEW_CAT0306("깔끔한 길"),
    REVIEW_CAT0307("운동 효과"),
    REVIEW_CAT0308("함께 걷는 사람"),
    REVIEW_CAT0309("사진 촬영"),
    REVIEW_CAT0310("초보자 친화적"),
    REVIEW_CAT0311("반려견 동행"),
    REVIEW_CAT0312("주변 식당・카페"),
    REVIEW_CAT0313("가벼운 산책"),
    REVIEW_CAT0314("좋은 교통환경");

    private final String category3;

    ReviewCategory3(String category3) {
        this.category3 = category3;
    }

    public ReviewCategory3 fromQuery(String query) {
        for (ReviewCategory3 category : ReviewCategory3.values()) {
            if (category.getCategory3().equals(query)) {
                return category;
            }
        }

        return null;
    }
}

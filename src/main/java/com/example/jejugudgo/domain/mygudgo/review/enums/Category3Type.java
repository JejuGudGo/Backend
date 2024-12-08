package com.example.jejugudgo.domain.mygudgo.review.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Category3Type {
    CATEGORY3_TYPE01("아름다운 경치"),
    CATEGORY3_TYPE02("안전한 길"),
    CATEGORY3_TYPE03("평온한 분위기"),
    CATEGORY3_TYPE04("많은 편의시설"),
    CATEGORY3_TYPE05("도전적"),
    CATEGORY3_TYPE06("깔끔한 길"),
    CATEGORY3_TYPE07("운동 효과"),
    CATEGORY3_TYPE08("함께 걷는 사람"),
    CATEGORY3_TYPE09("사진 촬영"),
    CATEGORY3_TYPE10("초보자 친화적"),
    CATEGORY3_TYPE11("반려견 동행"),
    CATEGORY3_TYPE12("주변 식당・카페"),
    CATEGORY3_TYPE13("가벼운 산책"),
    CATEGORY3_TYPE14("좋은 교통환경");

    private final String category3;

    Category3Type(String category3) {
        this.category3 = category3;
    }

    public static boolean isCategoryValid(String query) {
        return Arrays.stream(Category3Type.values())
                .anyMatch(type -> type.name().equalsIgnoreCase(query) || type.getCategory3().equals(query));
    }

    @Override
    public String toString() {
        return category3;
    }
}

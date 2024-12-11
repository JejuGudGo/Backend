package com.example.jejugudgo.domain.mygudgo.review.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Category1Type {
    CATEGORY1_TYPE01("체력 증진"),
    CATEGORY1_TYPE02("자연 감상"),
    CATEGORY1_TYPE03("도전・성취"),
    CATEGORY1_TYPE04("주변 관광"),
    CATEGORY1_TYPE05("사회적 교류"),
    CATEGORY1_TYPE06("스트레스 해소"),
    CATEGORY1_TYPE07("지인과의 시간");

    private final String category1;

    Category1Type(String category1) {
        this.category1 = category1;
    }

    public static boolean isCategoryValid(String query) {
        return Arrays.stream(Category1Type.values())
                .anyMatch(type -> type.name().equalsIgnoreCase(query) || type.getCategory1().equals(query));
    }
}

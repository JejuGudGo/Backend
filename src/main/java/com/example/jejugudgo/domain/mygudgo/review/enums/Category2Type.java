package com.example.jejugudgo.domain.mygudgo.review.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Category2Type {
    CATEGORY2_TYPE01("연인・배우자"),
    CATEGORY2_TYPE02("친구"),
    CATEGORY2_TYPE03("혼자"),
    CATEGORY2_TYPE04("가족"),
    CATEGORY2_TYPE05("반려동물"),
    CATEGORY2_TYPE06("직장동료"),
    CATEGORY2_TYPE07("제주걷고 동행");

    private final String category2;

    Category2Type(String category2) {
        this.category2 = category2;
    }

    public static boolean isCategoryValid(String query) {
        return Arrays.stream(Category2Type.values())
                .anyMatch(type -> type.name().equalsIgnoreCase(query) || type.getCategory2().equals(query));
    }
}

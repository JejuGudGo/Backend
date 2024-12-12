package com.example.jejugudgo.domain.course.recommend.enums;

import lombok.Getter;

@Getter
public enum RecommendType {
    RECOMMEND_TYPE01("관광명소"),
    RECOMMEND_TYPE02("문화시설"),
    RECOMMEND_TYPE03("숙박"),
    RECOMMEND_TYPE04("편의점"),
    RECOMMEND_TYPE05("식당/카페");

    String type;

    RecommendType(String type) {
        this.type = type;
    }
}

package com.example.jejugudgo.domain.course.common.enums;

import lombok.Getter;

@Getter
public enum OlleTag {
    OLLE_COURSE_TAG01("순환형"),
    OLLE_COURSE_TAG02("비순환형"),
    OLLE_COURSE_TAG03("바다"),
    OLLE_COURSE_TAG04("산"),
    OLLE_COURSE_TAG05("숲"),
    OLLE_COURSE_TAG06("골목길"),
    OLLE_COURSE_TAG07("도심"),
    OLLE_COURSE_TAG08("하영올레"),
    OLLE_COURSE_TAG09("제주올레"),
    OLLE_COURSE_TAG10("난이도 상"),
    OLLE_COURSE_TAG11("난이도 중"),
    OLLE_COURSE_TAG12("난이도 하");

    private final String tag;

    OlleTag(String tag) {
        this.tag = tag;
    }

    public static OlleTag fromInput(String input) {
        for (OlleTag olleTag : OlleTag.values()) {
            if (olleTag.getTag().equals(input)) {
                return olleTag;
            }
        }
        return null;
    }
}

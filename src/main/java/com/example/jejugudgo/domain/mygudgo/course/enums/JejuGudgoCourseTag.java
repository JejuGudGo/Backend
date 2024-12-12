package com.example.jejugudgo.domain.mygudgo.course.enums;

import lombok.Getter;

@Getter
public enum JejuGudgoCourseTag {
    JEJU_GUDGO_COURSE_TAG01("맛집탐방"),
    JEJU_GUDGO_COURSE_TAG02("감성카페"),
    JEJU_GUDGO_COURSE_TAG03("자연힐링"),
    JEJU_GUDGO_COURSE_TAG04("데이트"),
    JEJU_GUDGO_COURSE_TAG05("역사탐방"),
    JEJU_GUDGO_COURSE_TAG06("가족여행"),
    JEJU_GUDGO_COURSE_TAG07("예술과 문화"),
    JEJU_GUDGO_COURSE_TAG08("빵지순례"),
    JEJU_GUDGO_COURSE_TAG09("액티비티"),
    JEJU_GUDGO_COURSE_TAG10("챌린지코스"),
    JEJU_GUDGO_COURSE_TAG11("걷기크루"),
    JEJU_GUDGO_COURSE_TAG12("동네산책");

    private final String tag;

    JejuGudgoCourseTag(String tag) {
        this.tag = tag ;
    }

    public static JejuGudgoCourseTag fromCat2(String cat2) {
        for (JejuGudgoCourseTag courseTag : JejuGudgoCourseTag.values()) {
            if (courseTag.getTag().equals(cat2)) {
                return courseTag;
            }
        }
        return null;
    }
}

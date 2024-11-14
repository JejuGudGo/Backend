package com.example.jejugudgo.domain.course.jejugudgo.entity;

import lombok.Getter;

@Getter
public enum CourseTag {
    COURSE_TAG01("맛집탐방"),
    COURSE_TAG02("감성카페"),
    COURSE_TAG03("자연힐링"),
    COURSE_TAG04("데이트"),
    COURSE_TAG05("역사탐방"),
    COURSE_TAG06("가족여행"),
    COURSE_TAG07("예술과 문화"),
    COURSE_TAG08("빵지순례"),
    COURSE_TAG09("액티비티");

    private final String tag;

    CourseTag(String tag) {
        this.tag = tag;
    }

    public static CourseTag fromTag(String tag) {
        for (CourseTag courseTag : CourseTag.values()) {
            if (courseTag.getTag().equals(tag)) {
                return courseTag;
            }
        }

        return null;
    }
}

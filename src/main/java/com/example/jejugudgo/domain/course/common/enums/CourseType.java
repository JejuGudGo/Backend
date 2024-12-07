package com.example.jejugudgo.domain.course.common.enums;

import lombok.Getter;

@Getter
public enum CourseType {
    COURSE_TYPE01("제주객의 길"),
    COURSE_TYPE02("올레길"),
    COURSE_TYPE03("산책로");

    private final String type;

    CourseType(String type) {
        this.type = type;
    }

    public static CourseType fromCat1(String cat1) {
        for (CourseType type : CourseType.values()) {
            if (type.getType().equals(cat1)) {
                return type;
            }
        }
        return null;
    }
}

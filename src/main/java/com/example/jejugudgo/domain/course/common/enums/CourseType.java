package com.example.jejugudgo.domain.course.common.enums;

import lombok.Getter;

@Getter
public enum CourseType {
    COURSE_TYPE01("제주객의 길", "jeju_gudgo"),
    COURSE_TYPE02("올레길"," olle"),
    COURSE_TYPE03("산책로", "trail"),
    COURSE_TYPE04("전체", "all");

    private final String type;
    private final String pinKeyType;

    CourseType(String type, String pinKeyType) {
        this.type = type;
        this.pinKeyType = pinKeyType;
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

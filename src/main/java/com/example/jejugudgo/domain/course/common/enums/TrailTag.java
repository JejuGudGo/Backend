package com.example.jejugudgo.domain.course.common.enums;

import lombok.Getter;

@Getter
public enum TrailTag {
    TRAIL_TAG01("숲길"),
    TRAIL_TAG02("오름"),
    TRAIL_TAG03("해안"),
    TRAIL_TAG04("마을");

    private final String tag;

    TrailTag(String tag) {
        this.tag = tag;
    }

    public static TrailTag fromInput(String input) {
        for (TrailTag type : TrailTag.values()) {
            if (type.getTag().equals(input)) {
                return type;
            }
        }
        return null;
    }
}


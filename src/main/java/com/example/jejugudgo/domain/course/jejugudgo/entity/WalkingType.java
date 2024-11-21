package com.example.jejugudgo.domain.course.jejugudgo.entity;

import lombok.Getter;

@Getter
public enum WalkingType {
    WALKING_TYPE01("추천"),
    WALKING_TYPE02("큰길우선"),
    WALKING_TYPE03("최단거리"),
    WALKING_TYPE04("계단제외");

    private final String type;

    WalkingType(String type) {
        this.type = type;
    }

    public static WalkingType fromType(String type) {
        for (WalkingType walkingType : WalkingType.values()) {
            if (walkingType.getType().equals(type)) {
                return walkingType;
            }
        }
        return null;
    }

}

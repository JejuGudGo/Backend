package com.example.jejugudgo.domain.trail.entity;

import lombok.Getter;

@Getter
public enum TrailType {
    TRAIL01("숲길"),
    TRAIL02("오름"),
    TRAIL03("해안"),
    TRAIL04("마을");

    private final String code;

    TrailType(String code) {
        this.code = code;
    }

    public static TrailType fromCode(String code) {
        for (TrailType type : TrailType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}

package com.example.jejugudgo.domain.course.jejugudgo.entity;

import lombok.Getter;

@Getter
public enum SpotType {
    TOUR("투어API"),
    SEARCH("검색");

    private final String type;

    SpotType(String type) {
        this.type = type;
    }

    public static SpotType fromType(String type) {
        for (SpotType spotType : SpotType.values()) {
            if (spotType.getType().equals(type)) {
                return spotType;
            }
        }
        return null;
    }
}

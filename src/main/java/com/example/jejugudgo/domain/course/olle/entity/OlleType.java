package com.example.jejugudgo.domain.course.olle.entity;

import lombok.Getter;

@Getter
public enum OlleType {
    JEJU("제주올레"),
    HAYOUNG("하영올레");

    private final String type;

    OlleType(String type) {
        this.type = type;
    }

    public static OlleType fromType(String type) {
        for (OlleType olleType : OlleType.values()) {
            if (olleType.getType().equals(type)) {
                return olleType;
            }
        }
        return null;
    }
}

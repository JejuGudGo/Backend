package com.example.jejugudgo.domain.course.common.enums;

import lombok.Getter;

@Getter
public enum OlleType {
    OLLE_TYPE01("제주올레"),
    OLLE_TYPE02("하영올레");

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

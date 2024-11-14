package com.example.jejugudgo.domain.user.myGudgo.bookmark.entity;

import lombok.Getter;

@Getter
public enum BookMarkType {
    JEJU_GUDGO("제주객의길"),
    OLLE("올레길"),
    TRAIL("산책로");

    private final String code;

    BookMarkType(String code) {
        this.code = code;
    }

    public static BookMarkType fromCode(String code) {
        for (BookMarkType type : BookMarkType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }


}

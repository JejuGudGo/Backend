package com.example.jejugudgo.domain.user.myGudgo.bookmark.entity;

import lombok.Getter;

@Getter
public enum BookmarkType {
    JEJU_GUDGO("제주객의 길"),
    OLLE("올레길"),
    TRAIL("산책로");

    private final String code;

    BookmarkType(String code) {
        this.code = code;
    }

    public static BookmarkType fromCode(String code) {
        for (BookmarkType type : BookmarkType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }


}

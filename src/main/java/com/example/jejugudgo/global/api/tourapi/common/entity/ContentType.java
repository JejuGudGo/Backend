package com.example.jejugudgo.global.api.tourapi.common.entity;

import lombok.Getter;

import java.util.List;

@Getter
public enum ContentType {
    CONTENT_TYPE_ID_12("12", "관광지"),
    CONTENT_TYPE_ID_14("14", "문화시설"),
    CONTENT_TYPE_ID_15("15", "축제"),
    CONTENT_TYPE_ID_28("28", "레포츠"),
    CONTENT_TYPE_ID_38("38", "쇼핑"),
    CONTENT_TYPE_ID_39("39", "음식");

    private final String contentTypeId;
    private final String title;

    ContentType(String contentTypeId, String title) {
        this.contentTypeId = contentTypeId;
        this.title = title;
    }

    public static ContentType fromTitle(String title) {
        for (ContentType contentType : ContentType.values()) {
            if (contentType.getTitle().equals(title)) {
                return contentType;
            }
        }
        return null;
    }

    public static ContentType fromContentTypeId(String contentTypeId) {
        for (ContentType contentType : ContentType.values()) {
            if (contentType.getContentTypeId().equals(contentTypeId)) {
                return contentType;
            }
        }
        return null;
    }

    public static List<ContentType> getAllContentTypeIds() {
        return List.of(ContentType.values());
    }
}

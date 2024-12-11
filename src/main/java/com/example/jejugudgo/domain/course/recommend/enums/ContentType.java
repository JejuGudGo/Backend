package com.example.jejugudgo.domain.course.recommend.enums;

import lombok.Getter;

@Getter
public enum ContentType {
    CONTENT_TYPE12("12", "관광지"),
    CONTENT_TYPE14("14", "문화시설"),
    CONTENT_TYPE32("32", "숙박"),
    CONTENT_TYPE39("39", "식당/카페");

    private final String contentTypeId;
    private final String contentTypeName;

    ContentType(String contentTypeId, String contentTypeName) {
        this.contentTypeId = contentTypeId;
        this.contentTypeName = contentTypeName;
    }

    public static ContentType fromContentTypeId(String contentTypeId) {
        for (ContentType contentType : ContentType.values()) {
            if (contentType.contentTypeId.equals(contentTypeId)) {
                return contentType;
            }
        }
        return null;
    }
}

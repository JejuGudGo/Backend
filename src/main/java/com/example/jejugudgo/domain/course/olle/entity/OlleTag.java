package com.example.jejugudgo.domain.course.olle.entity;

import lombok.Getter;

@Getter
public enum OlleTag {
    OLLE_TAG01("순환형"),
    OLLE_TAG02("비순환형"),
    OLLE_TAG03("바다"),
    OLLE_TAG04("산"),
    OLLE_TAG05("숲"),
    OLLE_TAG06("골목길"),
    OLLE_TAG07("도심"),
    OLLE_TAG08("하영올레"),
    OLLE_TAG09("제주올레"),
    OLLE_TAG10("난이도 상"),
    OLLE_TAG11("난이도 중"),
    OLLE_TAG12("난이도 하");

    private final String tag;

    OlleTag(String tag) {
        this.tag = tag;
    }

    public static OlleTag fromTag(String tag) {
        for (OlleTag olleTag : OlleTag.values()) {
            if (olleTag.getTag().equals(tag)) {
                return olleTag;
            }
        }
        return null;
    }
}

package com.example.jejugudgo.domain.search.dto;

import java.util.List;

public record SearchListResponse(
        Long id,
        String type, // 제주걷고, 산책로, 제주올레, 하영올레
        List<String> tags, // 화면 명세상 태그
        boolean isBookmarked, // 즐겨찾기 여부
        String title,
        String summary, // 시작점 - 종점
        String distance,
        String time,
        String imgUrl,
        double starAvg,
        int reviewCount,
//        String content,
        String startSpotTitle,
        double startSpotLatitude,
        double startSpotLongitude
) {
}

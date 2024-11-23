package com.example.jejugudgo.domain.search.dto;

import java.util.List;

public record SearchListResponse(
        Long id,
        String type, // 올레길, 제주객의 길, 산책로
        List<String> tags, // 화면 명세상 태그
        boolean isBookmarked, // 즐겨찾기 여부
        Long bookmarkId,
        String title,
        String course, // 시작점 - 종점
        String summary,
        String distance,
        String time,
        String imgUrl,
        Double starAvg,
        Long reviewCount,
//        String content,
        String startSpotTitle,
        Double startSpotLatitude,
        Double startSpotLongitude
) {
}

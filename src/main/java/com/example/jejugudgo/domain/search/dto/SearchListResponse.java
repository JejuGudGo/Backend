package com.example.jejugudgo.domain.search.dto;

import com.example.jejugudgo.domain.search.dto.sub.SpotResponse;

import java.util.List;

public record SearchListResponse(
        Long id,
        String type,
        List<String> tags, // 화면 명세상 태그
        boolean isBookmarked, // 즐겨찾기 여부
        String title,
        String summary, // 시작점 - 종점
        String distance,
        String time,
        String starAvg,
        int reviewCount,
        String content,
        List<SpotResponse> spots
) {
}

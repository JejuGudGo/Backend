package com.example.jejugudgo.domain.search.dto.sub;

import java.util.List;

public record CourseBasicResponse(
        Long id,
        String type,
        List<String> tags, // 화면 명세상 태그
        boolean isBookmarked, // 즐겨찾기 여부
        String imageUrl,
        String title,
        String summary, // 시작점-종점
        String distance,
        String time,
        double starAvg,
        int reviewCount
) {
}

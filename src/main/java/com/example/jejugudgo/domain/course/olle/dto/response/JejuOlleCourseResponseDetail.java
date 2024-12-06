package com.example.jejugudgo.domain.course.olle.dto.response;

import java.util.List;

public record JejuOlleCourseResponseDetail(
        Long jejuOlleCourseId,
        String title,
        String startSpot,
        String endSpot,
        String totalTime,
        double starAvg,
        int reviewCount,
        // 태그 리스폰스 (tagResponse)

        // 스팟 리스폰스 (spotReseponse)
        List<JejuOlleSpotResponse> jejuOlleSpots,
        // 설명
        String summary,
        // 안내소 관련
        String infoAddress,
        String infoOpenTime,
        String infoPhone,
        String imageUrl
) {

}

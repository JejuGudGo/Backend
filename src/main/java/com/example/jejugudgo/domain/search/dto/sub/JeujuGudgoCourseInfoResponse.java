package com.example.jejugudgo.domain.search.dto.sub;

import java.util.List;

public record JeujuGudgoCourseInfoResponse(
        List<SpotResponse> spots,
        String content, // 제주객이 작성한 코스 설명
        SpotResponse startSpot,
        SpotResponse endSpot
) {
}

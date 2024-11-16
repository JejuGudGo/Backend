package com.example.jejugudgo.domain.search.dto.sub;

import java.util.List;

public record OlleCourseInfoResponse(
    List<SpotResponse> spots,
    String content, // 제주 올레 코스를 위한 설명
    String infoAddress,
    String infoOpenTime,
    String infoPhone,
    SpotResponse startSpot,
    SpotResponse endSpot
) {
}

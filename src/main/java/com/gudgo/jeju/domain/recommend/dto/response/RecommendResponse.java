package com.gudgo.jeju.domain.recommend.dto.response;

import java.time.LocalDateTime;

public record RecommendResponse(
        Long recommendId,
        String title1,
        String author,
        LocalDateTime createdAt,
        String title2,
        String section1Title,
        String section2Title,
        String section3Title,
        String section4Title,
        String section1Content,
        String section2Content,
        String section3Content,
        String section4Content


) {
}

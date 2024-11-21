package com.example.jejugudgo.domain.review.dto.request;

import java.time.LocalDate;
import java.util.List;

public record ReviewCreateRequest(
        String content,
        LocalDate finishedAt,
        Long stars,
        List<String> reviewCategory1,
        List<String> reviewCategory2,
        List<String> reviewCategory3
) {
}

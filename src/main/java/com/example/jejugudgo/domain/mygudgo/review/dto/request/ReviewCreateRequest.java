package com.example.jejugudgo.domain.mygudgo.review.dto.request;

import java.time.LocalDate;
import java.util.List;

public record ReviewCreateRequest(
        String courseType,
        Long targetId,
        LocalDate finishedAt,
        double star,
        List<String> reviewCat1,
        List<String> reviewCat2,
        List<String> reviewCat3,
        String content
) {
}

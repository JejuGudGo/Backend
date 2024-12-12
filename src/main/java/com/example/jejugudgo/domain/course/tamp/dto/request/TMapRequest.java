package com.example.jejugudgo.domain.course.tamp.dto.request;

public record TMapRequest(
        Long searchOption,
        String startName,
        double startX,
        double startY,
        String endName,
        double endX,
        double endY,
        String passList

) {
}

package com.gudgo.jeju.domain.post.walk.dto.response;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record CoursePostCreateResponse(
        Long postId,
        String nickName,
        String profileImgUrl,
        LocalDateTime createAt,
        LocalDate startDate,
        LocalTime startAt,
        Long maxCompanionsNum,
        LocalTime time,
        String placeName,
        String content,
        List<CoursePostSpotResponse> spots
) {
}

package com.gudgo.jeju.domain.post.walk.dto.request;


import java.time.LocalDate;
import java.time.LocalTime;

public record CoursePostCreateRequest(
        String title,
        LocalDate startDate, // 동생 시작 일
        LocalTime startTime, // 동행 시작 시간
        Long participantNum,
        String content,
        Long selectedPlannerId,
        String placeName,
        double placeLatitude,
        double placeLongitude
) {
}

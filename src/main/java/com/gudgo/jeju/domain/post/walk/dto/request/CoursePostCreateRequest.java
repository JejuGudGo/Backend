package com.gudgo.jeju.domain.post.walk.dto.request;


public record CoursePostCreateRequest(
        Long userId,
        String title,
        Long companionsNum, // 유저가 지정한 숫자
        String content,
        Long plannerId,
        Long olleCourseId,
        String placeName,
        double placeLatitude,
        double placeLongitude
) {
}
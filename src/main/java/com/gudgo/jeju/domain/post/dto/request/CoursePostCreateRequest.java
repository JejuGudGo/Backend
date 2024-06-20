package com.gudgo.jeju.domain.post.dto.request;


public record CoursePostCreateRequest(
        Long userId,
        Long courseId,
        String title,
        Long companionsNum, // 유저가 지정한 숫자
        String content
) {
}

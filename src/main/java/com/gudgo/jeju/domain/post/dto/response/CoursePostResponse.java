package com.gudgo.jeju.domain.post.dto.response;

import com.gudgo.jeju.domain.post.entity.PostType;

import java.time.LocalDate;

public record CoursePostResponse(
        Long id,
        Long userId,
        String Content,
        String title,
        PostType postType,
        Long courseId,
        Long companionsNum,
        LocalDate createdAt
) {
}

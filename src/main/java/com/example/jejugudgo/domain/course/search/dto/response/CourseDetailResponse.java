package com.example.jejugudgo.domain.course.search.dto.response;

import javax.annotation.Nullable;

public record CourseDetailResponse(
        Object basicData,
        Object infoData,
        @Nullable Object reviewData // TODO : 나중에 해제하기
) {
}

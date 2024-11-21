package com.example.jejugudgo.domain.search.dto;

import com.example.jejugudgo.domain.search.dto.sub.CourseBasicResponse;
import com.example.jejugudgo.domain.search.dto.sub.JeujuGudgoCourseInfoResponse;
import com.example.jejugudgo.domain.search.dto.sub.OlleCourseInfoResponse;
import com.example.jejugudgo.domain.review.dto.response.TopFiveRankedKeywordResponse;

import java.util.List;

public record SearchDetailResponse(
        CourseBasicResponse courseBasicInfo,
        JeujuGudgoCourseInfoResponse jeujuGudgoCourseInfo, // 제주걷고 코스의 경우
        OlleCourseInfoResponse olleCourseInfo, // 올레 코스의 경우
        List<TopFiveRankedKeywordResponse> keywords
) {
}

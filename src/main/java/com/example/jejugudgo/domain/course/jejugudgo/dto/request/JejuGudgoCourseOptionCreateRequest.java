package com.example.jejugudgo.domain.course.jejugudgo.dto.request;

import com.example.jejugudgo.domain.course.jejugudgo.dto.response.JejuGudgoCourseOptionResponse;

import java.util.List;

public record JejuGudgoCourseOptionCreateRequest(
        List<JejuGudgoCourseOptionResponse> jejuGudgoOptions
) {
}

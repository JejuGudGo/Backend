package com.example.jejugudgo.domain.mygudgo.course.dto.request;

import com.example.jejugudgo.domain.mygudgo.course.dto.response.SpotInfo;

import java.util.List;

public record SpotInfoRequest(
        String searchOption,
        List<SpotInfo> spots
) {
}

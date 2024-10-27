package com.example.jejugudgo.domain.bookmark.dto.response;

import com.example.jejugudgo.domain.course.dto.response.JejuGudgoCourseResponseForList;
import com.example.jejugudgo.domain.course.dto.response.JejuOlleCourseResponseForList;
import com.example.jejugudgo.domain.trail.dto.TrailResponseForList;

public record BookMarkResponse(
        Long bookmarkId,
        JejuOlleCourseResponseForList jejuOlleCourseResponseForList,
        JejuGudgoCourseResponseForList jejuGudgoCourseResponseForList,
        TrailResponseForList trailResponseForList

) {

}

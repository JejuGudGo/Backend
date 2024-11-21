package com.example.jejugudgo.domain.user.myGudgo.bookmark.dto.response;

import com.example.jejugudgo.domain.course.jejugudgo.dto.response.JejuGudgoCourseResponseForList;
import com.example.jejugudgo.domain.course.olle.dto.response.JejuOlleCourseResponseForList;
import com.example.jejugudgo.domain.trail.dto.TrailResponseForList;

public record BookmarkResponse(
        Long bookmarkId,
        JejuOlleCourseResponseForList jejuOlleCourseResponseForList,
        JejuGudgoCourseResponseForList jejuGudgoCourseResponseForList,
        TrailResponseForList trailResponseForList

) {

}

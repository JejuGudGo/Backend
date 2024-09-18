package com.gudgo.jeju.domain.bookmark.dto.request;

import com.gudgo.jeju.domain.planner.course.entity.CourseType;
import com.gudgo.jeju.domain.trail.entity.TrailType;

public record FilterDto(
        CourseType courseType,
        TrailType trailType
) {

}

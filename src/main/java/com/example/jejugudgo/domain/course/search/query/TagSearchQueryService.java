package com.example.jejugudgo.domain.course.search.query;

import com.example.jejugudgo.domain.course.search.dto.request.CourseSearchRequest;
import com.example.jejugudgo.domain.course.search.dto.response.CourseSearchResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface TagSearchQueryService<T> {
    public List<CourseSearchResponse> getCourses(HttpServletRequest httpRequest, CourseSearchRequest request);
    public List<T> getCoursesByCategory(CourseSearchRequest request);
}

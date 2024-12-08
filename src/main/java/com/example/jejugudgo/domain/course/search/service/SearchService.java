package com.example.jejugudgo.domain.course.search.service;

import com.example.jejugudgo.domain.course.search.dto.request.CourseSearchRequest;
import com.example.jejugudgo.domain.course.search.dto.response.CourseSearchResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface SearchService {
    public List<CourseSearchResponse> getCourses(HttpServletRequest httpRequest, CourseSearchRequest request);
}

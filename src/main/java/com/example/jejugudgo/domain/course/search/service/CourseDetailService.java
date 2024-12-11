package com.example.jejugudgo.domain.course.search.service;

import com.example.jejugudgo.domain.course.search.dto.request.CourseRequest;
import com.example.jejugudgo.domain.course.search.dto.response.CourseDetailResponse;
import jakarta.servlet.http.HttpServletRequest;


public interface CourseDetailService {
    public CourseDetailResponse getCourseDetail(HttpServletRequest httpRequest, CourseRequest request);
    public Object getBasicData(HttpServletRequest httpRequest, CourseRequest request);
    public Object getInfoData(HttpServletRequest httpRequest, CourseRequest request);
    public Object getReviewData(HttpServletRequest httpRequest, CourseRequest request);
}

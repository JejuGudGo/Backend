package com.example.jejugudgo.domain.course.search.service;

import com.example.jejugudgo.domain.course.search.dto.request.CourseDetailRequest;
import com.example.jejugudgo.domain.course.search.dto.response.CourseDetailResponse;
import jakarta.servlet.http.HttpServletRequest;


public interface CourseDetailService {
    public CourseDetailResponse getCourseDetail(HttpServletRequest httpRequest, CourseDetailRequest request);
    public Object getBasicData(HttpServletRequest httpRequest, CourseDetailRequest request);
    public Object getInfoData(HttpServletRequest httpRequest, CourseDetailRequest request);
    public Object getReviewData(HttpServletRequest httpRequest, CourseDetailRequest request);
}

package com.example.jejugudgo.domain.course.search.service;

import com.example.jejugudgo.domain.course.common.repository.JejuGudgoCourseRepository;
import com.example.jejugudgo.domain.course.search.dto.request.CourseRequest;
import com.example.jejugudgo.domain.course.search.dto.response.CourseDetailResponse;
import com.example.jejugudgo.domain.mygudgo.course.repository.UserJejuGudgoCourseRepository;
import com.example.jejugudgo.domain.mygudgo.like.util.UserLikeUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JejuGudgoDetailService implements CourseDetailService{
    private final UserLikeUtil userLikeUtil;
    private final JejuGudgoCourseRepository jejuGudgoCourseRepository;
    private final UserJejuGudgoCourseRepository userJejuGudgoCourseRepository;

    @Override
    public CourseDetailResponse getCourseDetail(HttpServletRequest httpRequest, CourseRequest request) {
        return null;
    }

    @Override
    public Object getBasicData(HttpServletRequest httpRequest, CourseRequest request) {
        return null;
    }

    @Override
    public Object getInfoData(HttpServletRequest httpRequest, CourseRequest request) {
        return null;
    }

    @Override
    public Object getReviewData(HttpServletRequest httpRequest, CourseRequest request) {
        return null;
    }
}

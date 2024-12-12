package com.example.jejugudgo.domain.mygudgo.course.service;

import com.example.jejugudgo.domain.mygudgo.course.dto.request.GenerateWalkingPathRequest;
import com.example.jejugudgo.domain.mygudgo.course.dto.request.UserCourseCreateRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCourseService {

    @Transactional
    public void create(HttpServletRequest httpRequest, UserCourseCreateRequest userCourseCreateRequest) {

    }

    public void generateWalkingPath(GenerateWalkingPathRequest generateWalkingPathRequest) {

    }
}

package com.example.jejugudgo.domain.course.jejugudgo.service;

import com.example.jejugudgo.domain.course.jejugudgo.dto.request.JejuGudgoCourseUpdateRequest;
import com.example.jejugudgo.domain.course.jejugudgo.dto.response.JejuGudgoCourseUpdateResponse;
import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourse;
import com.example.jejugudgo.domain.course.jejugudgo.repository.JejuGudgoCourseRepository;
import com.example.jejugudgo.domain.user.course.jejuGudgo.entity.UserJejuGudgoCourse;
import com.example.jejugudgo.domain.user.course.jejuGudgo.repository.UserJejuGudgoCourseRepository;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.util.ImageUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JejuGudgoUpdateService {


    private final JejuGudgoCourseRepository jejuGudgoCourseRepository;
    private final ImageUtil imageUtil;
    private final UserJejuGudgoCourseRepository userJejuGudgoCourseRepository;

    @Transactional
    public JejuGudgoCourseUpdateResponse updateUserCourse(
            Long courseId,
            HttpServletRequest servletRequest,
            JejuGudgoCourseUpdateRequest updateRequest) throws Exception {

        // 1. JejuGudgoCourse 조회
        JejuGudgoCourse jejuGudgoCourse = jejuGudgoCourseRepository.findById(courseId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE12)); // 코스가 없으면 예외 발생

        // 2. 이미지 처리
        final String newImageUrl; // final로 선언
        if (updateRequest.image() != null) {
            Long userId = jejuGudgoCourse.getUser().getId(); // JejuGudgoCourse에서 유저 ID 가져옴
            newImageUrl = imageUtil.saveImage(userId, updateRequest.image(), "course").toString();
        } else {
            newImageUrl = null;
        }

        // 3. JejuGudgoCourse 업데이트
        JejuGudgoCourse newJejuGudgoCourse = jejuGudgoCourse.updateDetails(newImageUrl, updateRequest.title(), updateRequest.content());
        jejuGudgoCourseRepository.save(newJejuGudgoCourse);

        // 4. 연관된 UserJejuGudgoCourse 업데이트 (람다식 대신 for 문 사용)
        List<UserJejuGudgoCourse> userCourses = userJejuGudgoCourseRepository.findByJejuGudgoCourseId(courseId);
        for (UserJejuGudgoCourse userCourse : userCourses) {
            UserJejuGudgoCourse updatedCourse = userCourse.updateDetails(newImageUrl, updateRequest.title(), updateRequest.content());
            userJejuGudgoCourseRepository.save(updatedCourse);
        }

        // 5. 응답 생성
        return new JejuGudgoCourseUpdateResponse(
                newJejuGudgoCourse.getImageUrl(),
                newJejuGudgoCourse.getTitle(),
                newJejuGudgoCourse.getContent()
        );
    }

}

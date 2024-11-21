package com.example.jejugudgo.domain.course.jejugudgo.service;

import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourse;
import com.example.jejugudgo.domain.course.jejugudgo.repository.JejuGudgoCourseRepository;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.jwt.token.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JejuGudgoDeleteService {

    private final JejuGudgoCourseRepository jejuGudgoCourseRepository;
    private final TokenUtil tokenUtil;

    @Transactional
    public void deleteJejuGudgoCourse(Long courseId, HttpServletRequest servletRequest) {
        JejuGudgoCourse course = jejuGudgoCourseRepository.findById(courseId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        Long userId = tokenUtil.getUserIdFromHeader(servletRequest);
        if (!course.getUser().getId().equals(userId)) {
            throw new CustomException(RetCode.RET_CODE15);
        }

        JejuGudgoCourse updatedCourse = course.updateDeleted(true);
        jejuGudgoCourseRepository.save(updatedCourse);

    }
}

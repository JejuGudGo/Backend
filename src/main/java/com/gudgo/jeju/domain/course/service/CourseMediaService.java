package com.gudgo.jeju.domain.course.service;


import com.gudgo.jeju.domain.course.dto.request.CourseMediaCreateRequestDto;
import com.gudgo.jeju.domain.course.dto.request.course.CourseMediaUpdateRequestDto;
import com.gudgo.jeju.domain.course.dto.response.CourseMediaResponseDto;
import com.gudgo.jeju.domain.course.entity.Course;
import com.gudgo.jeju.domain.course.entity.CourseMedia;
import com.gudgo.jeju.domain.course.repository.CourseMediaRepository;
import com.gudgo.jeju.domain.course.repository.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CourseMediaService {

    private final CourseMediaRepository courseMediaRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public void newCourseMedia(CourseMediaCreateRequestDto requestDto) {

        Course course = courseRepository.findById(requestDto.courseId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + requestDto.courseId()));

        CourseMedia courseMedia = CourseMedia.builder()
                .course(course)
                .imageUrl(requestDto.imageUrl())
                .content(requestDto.content())
                .latitude(requestDto.latitude())
                .longitude(requestDto.longitude())
                .build();

        courseMediaRepository.save(courseMedia);
    }

    @Transactional(readOnly = true)
    public List<CourseMediaResponseDto> getCourseMediasByCourseId(Long courseId) {
        Optional<CourseMedia> medias = courseMediaRepository.findById(courseId);
        return medias.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CourseMediaResponseDto getCourseMedia(Long id) {
        CourseMedia courseMedia = courseMediaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CourseMedia not found with id: " + id));
        return convertToDto(courseMedia);
    }

    @Transactional
    public void updateCourseMedia(Long id, CourseMediaUpdateRequestDto requestDto) {
        CourseMedia courseMedia = courseMediaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CourseMedia not found with id: " + id));
        CourseMedia updatedCourseMedia = courseMedia.withContentAndImageUrl(requestDto);
        courseMediaRepository.save(updatedCourseMedia);
    }

    private CourseMediaResponseDto convertToDto(CourseMedia courseMedia) {
        return new CourseMediaResponseDto(
                courseMedia.getId(),
                courseMedia.getCourse().getId(),
                courseMedia.getContent(),
                courseMedia.getImageUrl(),
                courseMedia.getLatitude(),
                courseMedia.getLongitude()

        );
    }
}

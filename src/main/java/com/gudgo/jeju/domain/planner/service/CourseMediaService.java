package com.gudgo.jeju.domain.planner.service;


import com.gudgo.jeju.domain.planner.dto.request.course.CourseMediaCreateRequestDto;
import com.gudgo.jeju.domain.planner.dto.request.course.CourseMediaUpdateRequestDto;
import com.gudgo.jeju.domain.planner.dto.response.CourseMediaResponseDto;
import com.gudgo.jeju.domain.planner.entity.Course;
import com.gudgo.jeju.domain.planner.entity.CourseMedia;
import com.gudgo.jeju.domain.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.query.CourseMediaQueryService;
import com.gudgo.jeju.domain.planner.repository.CourseMediaRepository;
import com.gudgo.jeju.domain.planner.repository.CourseRepository;
import com.gudgo.jeju.domain.planner.repository.PlannerRepository;
import com.gudgo.jeju.global.util.ValidationUtil;
import com.gudgo.jeju.global.util.image.entity.Category;
import com.gudgo.jeju.global.util.image.service.ImageDeleteService;
import com.gudgo.jeju.global.util.image.service.ImageUpdateService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CourseMediaService {
    private final CourseMediaRepository courseMediaRepository;
    private final PlannerRepository plannerRepository;

    private final CourseMediaQueryService courseMediaQueryService;
    private final ImageUpdateService imageUpdateService;
    private final ImageDeleteService imageDeleteService;
    private final ValidationUtil validationUtil;

    @Transactional(readOnly = true)
    public List<CourseMediaResponseDto> getMedias(Long plannerId) {
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(EntityNotFoundException::new);

        List<CourseMediaResponseDto> courseMediaResponseDtos = courseMediaQueryService.getMedias(planner.getCourse().getId());

        return courseMediaResponseDtos;
    }

    @Transactional
    public void create(Long userId, Long plannerId, MultipartFile image, CourseMediaCreateRequestDto requestDto) throws Exception {
        Path path = imageUpdateService.saveImage(userId, image, Category.USERCOURSE);

        Planner planner = plannerRepository.findByCourseId(plannerId)
                .orElseThrow(EntityNotFoundException::new);

        CourseMedia courseMedia = CourseMedia.builder()
                .planner(planner)
                .imageUrl(path.toString())
                .content(requestDto.content())
                .latitude(requestDto.latitude())
                .longitude(requestDto.longitude())
                .isDeleted(false)
                .build();

        courseMediaRepository.save(courseMedia);
    }

    @Transactional(readOnly = true)
    public CourseMediaResponseDto getMedia(Long mediaId) {
        CourseMedia courseMedia = courseMediaRepository.findById(mediaId)
                .orElseThrow(EntityNotFoundException::new);

        return convertToDto(courseMedia);
    }

    @Transactional
    public void update(Long userId, Long mediaId, MultipartFile image, CourseMediaUpdateRequestDto requestDto) throws Exception {
        CourseMedia courseMedia = courseMediaRepository.findById(mediaId)
                .orElseThrow(EntityNotFoundException::new);

        if (!image.isEmpty()) {
            imageDeleteService.deleteImageWithUrl(courseMedia.getImageUrl());
            Path path = imageUpdateService.saveImage(userId, image, Category.USERCOURSE);

            courseMedia = courseMedia.withImageUrl(path.toString());
        }

        if (validationUtil.validateStringValue(requestDto.content())) {
            courseMedia = courseMedia.withContent(requestDto.content());
        }

        courseMediaRepository.save(courseMedia);
    }

    @Transactional
    public void delete(Long mediaId) {
        CourseMedia courseMedia = courseMediaRepository.findById(mediaId)
                .orElseThrow(EntityNotFoundException::new);

        courseMedia = courseMedia.withIsDeleted(true);

        courseMediaRepository.save(courseMedia);
    }

    private CourseMediaResponseDto convertToDto(CourseMedia courseMedia) {
        return new CourseMediaResponseDto(
                courseMedia.getId(),
                courseMedia.getPlanner().getId(),
                courseMedia.getContent(),
                courseMedia.getImageUrl(),
                courseMedia.getLatitude(),
                courseMedia.getLongitude()

        );
    }
}

package com.gudgo.jeju.domain.planner.courseMedia.service;


import com.gudgo.jeju.domain.planner.courseMedia.dto.request.CourseMediaCreateRequestDto;
import com.gudgo.jeju.domain.planner.courseMedia.dto.request.CourseMediaUpdateRequestDto;
import com.gudgo.jeju.domain.planner.courseMedia.dto.response.CourseMediaBackImagesResponseDto;
import com.gudgo.jeju.domain.planner.courseMedia.dto.response.CourseMediaResponseDto;
import com.gudgo.jeju.domain.planner.courseMedia.entity.CourseMedia;
import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.courseMedia.query.CourseMediaQueryService;
import com.gudgo.jeju.domain.planner.courseMedia.repository.CourseMediaRepository;
import com.gudgo.jeju.domain.planner.planner.repository.PlannerRepository;
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


    public List<CourseMediaResponseDto> getAllMedias(Long userId) {
        List<CourseMediaResponseDto> courseMediaResponseDtos = courseMediaQueryService.getAllMedias(userId);
        return courseMediaResponseDtos;
    }

    public List<CourseMediaBackImagesResponseDto> getAllBackImages(Long userId) {
        List<CourseMediaBackImagesResponseDto> courseMediaBackImagesResponseDtos = courseMediaQueryService.getAllBackImages(userId);
        return courseMediaBackImagesResponseDtos;
    }

    @Transactional
    public void create(Long userId, Long plannerId, MultipartFile selfieImage, MultipartFile backImage, CourseMediaCreateRequestDto requestDto) throws Exception {
        Path path1 = imageUpdateService.saveImage(userId, selfieImage, Category.USERCOURSE);
        Path path2 = imageUpdateService.saveImage(userId, backImage, Category.USERCOURSE);

        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(EntityNotFoundException::new);

        CourseMedia courseMedia = CourseMedia.builder()
                .planner(planner)
                .selfieImageUrl(path1.toString())
                .backImageUrl(path2.toString())
                .content(requestDto.content())
                .latitude(requestDto.latitude())
                .longitude(requestDto.longitude())
                .isDeleted(false)
                .build();

        courseMediaRepository.save(courseMedia);
    }


    @Transactional(readOnly = true)
    public CourseMediaResponseDto getMedia(Long userId,Long mediaId) {
        CourseMedia courseMedia = courseMediaRepository.findById(mediaId)
                .orElseThrow(EntityNotFoundException::new);

        return convertToDto(courseMedia);
    }

    @Transactional
    public void update(Long userId, Long plannerId, Long mediaId, MultipartFile selfImage, MultipartFile backImage, CourseMediaUpdateRequestDto requestDto) throws Exception {
        CourseMedia courseMedia = courseMediaRepository.findById(mediaId)
                .orElseThrow(EntityNotFoundException::new);

        if (!selfImage.isEmpty()) {
            imageDeleteService.deleteImageWithUrl(courseMedia.getSelfieImageUrl());
            Path path = imageUpdateService.saveImage(userId, selfImage, Category.USERCOURSE);

            courseMedia = courseMedia.withSelfieImageUrl(path.toString());
        }
        if (!backImage.isEmpty()) {
            imageDeleteService.deleteImageWithUrl(courseMedia.getBackImageUrl());
            Path path = imageUpdateService.saveImage(userId, backImage, Category.USERCOURSE);

            courseMedia = courseMedia.withBackImageUrl(path.toString());
        }

        if (validationUtil.validateStringValue(requestDto.content())) {
            courseMedia = courseMedia.withContent(requestDto.content());
        }

        courseMediaRepository.save(courseMedia);
    }

    @Transactional
    public void delete(Long userId, Long plannerId, Long mediaId) {
        CourseMedia courseMedia = courseMediaRepository.findById(mediaId)
                .orElseThrow(EntityNotFoundException::new);

        courseMedia = courseMedia.withIsDeleted(true);

        courseMediaRepository.save(courseMedia);
    }
private CourseMediaResponseDto convertToDto(CourseMedia courseMedia) {
    return new CourseMediaResponseDto(
            courseMedia.getId(),
            courseMedia.getContent(),
            courseMedia.getSelfieImageUrl(),
            courseMedia.getBackImageUrl(),
            courseMedia.getLatitude(),
            courseMedia.getLongitude()

    );
}
}

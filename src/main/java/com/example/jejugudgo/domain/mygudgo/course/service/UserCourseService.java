package com.example.jejugudgo.domain.mygudgo.course.service;

import com.example.jejugudgo.domain.course.common.entity.JejuGudgoCourse;
import com.example.jejugudgo.domain.course.common.query.CourseQueryService;
import com.example.jejugudgo.domain.course.common.repository.JejuGudgoCourseRepository;
import com.example.jejugudgo.domain.mygudgo.course.dto.request.*;
import com.example.jejugudgo.domain.mygudgo.course.dto.response.UserCourseUpdateResponse;
import com.example.jejugudgo.domain.mygudgo.course.enums.SearchOption;
import com.example.jejugudgo.domain.course.tmap.service.WalkingPathService;
import com.example.jejugudgo.domain.mygudgo.course.dto.response.SpotInfo;
import com.example.jejugudgo.domain.mygudgo.course.dto.response.UserCourseCreateResponse;
import com.example.jejugudgo.domain.mygudgo.course.entity.UserJejuGudgoCourse;
import com.example.jejugudgo.domain.mygudgo.course.entity.UserJejuGudgoCourseTag;
import com.example.jejugudgo.domain.mygudgo.course.entity.UserJejuGudgoSpot;
import com.example.jejugudgo.domain.mygudgo.course.enums.JejuGudgoCourseTag;
import com.example.jejugudgo.domain.mygudgo.course.repository.UserJejuGudgoCourseRepository;
import com.example.jejugudgo.domain.mygudgo.course.repository.UserJejuGudgoCourseTagRepository;
import com.example.jejugudgo.domain.mygudgo.course.repository.UserJejuGudgoSpotRepository;
import com.example.jejugudgo.domain.user.common.entity.User;
import com.example.jejugudgo.domain.user.common.repository.UserRepository;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.jwt.token.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserCourseService {

    private final TokenUtil tokenUtil;
    private final UserRepository userRepository;
    private final UserJejuGudgoCourseRepository userJejuGudgoCourseRepository;
    private final UserJejuGudgoCourseTagRepository userJejuGudgoCourseTagRepository;
    private final UserJejuGudgoSpotRepository userJejuGudgoSpotRepository;
    private final WalkingPathService walkingPathService;
    private final JejuGudgoCourseRepository jejuGudgoCourseRepository;

    private final String DEFAULT_IMAGE_URL = "default";



    public UserCourseCreateResponse create(HttpServletRequest httpRequest, UserCourseCreateRequest userCourseCreateRequest) {
        // 사용자 검증
        User user = validateUser(httpRequest);

        // 최소, 최대 order의 spot 찾기
        List<SpotInfo> sortedSpots = sortSpots(userCourseCreateRequest.spotInfo());
        String route = generateRoute(sortedSpots);

        // UserJejuGudgoCourse 생성
        UserJejuGudgoCourse userCourse = createUserCourse(userCourseCreateRequest, user, route);

        // UserJejuGudgoCourseTag 생성
        saveCourseTags(userCourseCreateRequest, userCourse);

        // UserJejuGudgoSpot 생성
        saveCourseSpots(userCourseCreateRequest, userCourse);

        // 비동기 작업 호출
        // 각 SearchOption별로 WalkingPath 생성
        for (SearchOption searchOption : SearchOption.values()) {
            SpotInfoRequest spotInfoRequest = new SpotInfoRequest(
                    searchOption.getSearchOptionId(),
                    sortedSpots
            );
            GenerateWalkingPathRequest walkingPathRequest = new GenerateWalkingPathRequest(
                    userCourse.getId(),
                    spotInfoRequest
            );
            walkingPathService.generateAndSaveWalkingPath(walkingPathRequest);
        }

        // 응답 생성
        return buildResponse(userCourse, userCourseCreateRequest);
    }

    private User validateUser(HttpServletRequest httpRequest) {
        Long userId = tokenUtil.getUserIdFromHeader(httpRequest);
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE13));
    }

    private List<SpotInfo> sortSpots(List<SpotInfo> spots) {
        return spots.stream()
                .sorted(Comparator.comparing(SpotInfo::order))
                .collect(Collectors.toList());
    }

    private String generateRoute(List<SpotInfo> sortedSpots) {
        return sortedSpots.get(0).title() + "-" +
                sortedSpots.get(sortedSpots.size() - 1).title();
    }

    private UserJejuGudgoCourse createUserCourse(UserCourseCreateRequest request, User user, String route) {
        UserJejuGudgoCourse userCourse = UserJejuGudgoCourse.builder()
                .title(request.title())
                .content(request.content())
                .user(user)
                .createdAt(LocalDate.now())
                .isPrivate(false)
                .route(route)
                .isImported(false)
                .thumbnailUrl(DEFAULT_IMAGE_URL)
                .build();
        return userJejuGudgoCourseRepository.save(userCourse);
    }

    private void saveCourseTags(UserCourseCreateRequest request, UserJejuGudgoCourse userCourse) {
        var courseTags = request.tags().stream()
                .map(tag -> {
                    JejuGudgoCourseTag courseTag = JejuGudgoCourseTag.fromCat2(tag);
                    if (courseTag == null) {
                        throw new CustomException(RetCode.RET_CODE17);
                    }
                    return UserJejuGudgoCourseTag.builder()
                            .title(courseTag)
                            .userCourse(userCourse)
                            .build();
                })
                .collect(Collectors.toList());

        userJejuGudgoCourseTagRepository.saveAll(courseTags);
    }

    private void saveCourseSpots(UserCourseCreateRequest request, UserJejuGudgoCourse userCourse) {
        List<UserJejuGudgoSpot> spots = request.spotInfo().stream()
                .map(spot -> UserJejuGudgoSpot.builder()
                        .title(spot.title())
                        .latitude(spot.latitude())
                        .longitude(spot.longitude())
                        .spotOrder(spot.order())
                        .userCourse(userCourse)
                        .build())
                .collect(Collectors.toList());

        userJejuGudgoSpotRepository.saveAll(spots);
    }

    private UserCourseCreateResponse buildResponse(UserJejuGudgoCourse userCourse, UserCourseCreateRequest request) {
        return new UserCourseCreateResponse(
                userCourse.getId(),
                userCourse.getTitle(),
                request.tags(),
                userCourse.getContent(),
                request.spotInfo()
        );
    }

    @Transactional
    public UserCourseUpdateResponse update(HttpServletRequest httpRequest, UserCourseUpdateRequest userCourseUpdateRequest) {

        // request의 id값으로 jejuGudgoCourse 찾기
        JejuGudgoCourse jejuGudgoCourse = jejuGudgoCourseRepository.findById(userCourseUpdateRequest.id())
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));


        // 수정 권한 검사
        validatePermission(httpRequest, jejuGudgoCourse);

        // jejuGudgoCourse를 가진 모든 UserJejuGudgoCourse 불러오기
        List<UserJejuGudgoCourse> userCourses = userJejuGudgoCourseRepository.findByJejuGudgoCourse(jejuGudgoCourse);

        // 해당하는 값 수정하기
        userCourses.forEach(course -> {
            if (userCourseUpdateRequest.image() != null) {
                course = course.updateThumbnailUrl(userCourseUpdateRequest.image());
            }
            if (userCourseUpdateRequest.content() != null) {
                course = course.updateContent(userCourseUpdateRequest.content());
            }
            if (userCourseUpdateRequest.title() != null) {
                course = course.updateTitle(userCourseUpdateRequest.title());
            }
            userJejuGudgoCourseRepository.save(course);
        });


        UserJejuGudgoCourse userJejuGudgoCourse = userJejuGudgoCourseRepository.findByJejuGudgoCourseAndIsImportedFalse(jejuGudgoCourse)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        List<String> tags = getTagsForUserCourse(userJejuGudgoCourse);

        // 응답 생성
        return new UserCourseUpdateResponse(
                jejuGudgoCourse.getId(),
                userJejuGudgoCourse.getTitle(),
                tags,
                userJejuGudgoCourse.getContent(),
                userJejuGudgoCourse.getRoute(),
                userJejuGudgoCourse.getThumbnailUrl()
        );
    }

    @Transactional
    public void delete(HttpServletRequest httpRequest, IdRequest idRequest) {

        // isDeleted = true 처리
        JejuGudgoCourse jejuGudgoCourse = jejuGudgoCourseRepository.findById(idRequest.id())
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

        // 삭제 권한 검사
        validatePermission(httpRequest, jejuGudgoCourse);

        jejuGudgoCourse = jejuGudgoCourse.updateIsDeleted(true);
        jejuGudgoCourseRepository.save(jejuGudgoCourse);
    }

    /**
    * 수정 및 삭제 권한 검사
    * */
    private void validatePermission(HttpServletRequest httpRequest, JejuGudgoCourse jejuGudgoCourse) {
        Long userId = tokenUtil.getUserIdFromHeader(httpRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE13));
        if (!jejuGudgoCourse.getUser().equals(user)) {
            throw new CustomException(RetCode.RET_CODE15);
        }
    }

    private List<String> getTagsForUserCourse(UserJejuGudgoCourse userJejuGudgoCourse) {
        return userJejuGudgoCourseTagRepository.findByUserCourse(userJejuGudgoCourse).stream()
                .map(UserJejuGudgoCourseTag::getTitle)
                .map(JejuGudgoCourseTag::getTag)
                .collect(Collectors.toList());
    }
}
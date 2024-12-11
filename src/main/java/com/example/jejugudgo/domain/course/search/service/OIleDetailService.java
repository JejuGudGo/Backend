package com.example.jejugudgo.domain.course.search.service;

import com.example.jejugudgo.domain.course.common.dto.RoutePoint;
import com.example.jejugudgo.domain.course.common.entity.OlleCourse;
import com.example.jejugudgo.domain.course.common.entity.OlleCourseTag;
import com.example.jejugudgo.domain.course.common.entity.OlleSpot;
import com.example.jejugudgo.domain.course.common.repository.OlleCourseRepository;
import com.example.jejugudgo.domain.course.common.repository.OlleCourseTagRepository;
import com.example.jejugudgo.domain.course.common.repository.OlleSpotRepository;
import com.example.jejugudgo.domain.course.search.dto.request.CourseDetailRequest;
import com.example.jejugudgo.domain.course.search.dto.response.BasicData;
import com.example.jejugudgo.domain.course.search.dto.response.CourseDetailResponse;
import com.example.jejugudgo.domain.course.search.dto.response.OlleInfoData;
import com.example.jejugudgo.domain.mygudgo.like.dto.response.LikeInfo;
import com.example.jejugudgo.domain.mygudgo.like.util.UserLikeUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OIleDetailService implements CourseDetailService {
    private final UserLikeUtil userLikeUtil;
    private final OlleCourseRepository olleCourseRepository;
    private final OlleCourseTagRepository olleCourseTagRepository;
    private final OlleSpotRepository olleSpotRepository;

    @Override
    public CourseDetailResponse getCourseDetail(HttpServletRequest httpRequest, CourseDetailRequest request) {
        Object basicData = getBasicData(httpRequest, request);
        Object infoData = getInfoData(httpRequest, request);
        Object reviewData = getReviewData(httpRequest, request);

        return new CourseDetailResponse(
                basicData,
                infoData,
                null
        );
    }

    @Override
    public Object getBasicData(HttpServletRequest httpRequest, CourseDetailRequest request) {
        OlleCourse olleCourse = getOlleCourse(request);

        List<OlleCourseTag> olleCourseTags = olleCourseTagRepository.findByOlleCourse(olleCourse);

        List<String> courseTags = olleCourseTags.stream()
                .map(tag -> tag.getTitle().getTag())
                .toList();

        LikeInfo likeInfo = userLikeUtil.isLiked(httpRequest, request.cat1().getType(), request.id());

        return new BasicData(
                olleCourse.getId(),
                courseTags,
                likeInfo,
                olleCourse.getTitle(),
                olleCourse.getRoute(),
                olleCourse.getDistance(),
                olleCourse.getTime(),
                olleCourse.getStarAvg(),
                olleCourse.getReviewCount()
        );
    }

    @Override
    public Object getInfoData(HttpServletRequest httpRequest, CourseDetailRequest request) {
        OlleCourse olleCourse = getOlleCourse(request);
        List<OlleSpot> olleSpots = olleSpotRepository.findByOlleCourseOrderBySpotOrderAsc(olleCourse);
        int lastId = olleSpots.size() - 1;

        List<RoutePoint> spots = olleSpots.stream()
                .map(spot -> new RoutePoint(
                        spot.getTitle(),
                        spot.getLatitude(),
                        spot.getLongitude()
                ))
                .toList();

        return new OlleInfoData (
                spots,
                olleCourse.getContent(),
                spots.get(0),
                spots.get(lastId),
                olleCourse.getAddress(),
                olleCourse.getOpenTime(),
                olleCourse.getTel()
        );
    }

    // TODO : 리뷰 기능 생성시 작성하기
    @Override
    public Object getReviewData(HttpServletRequest httpRequest, CourseDetailRequest request) {
        return null;
    }

    private OlleCourse getOlleCourse(CourseDetailRequest request) {
        return olleCourseRepository.findById(request.id())
                .orElse(null);
    }
}

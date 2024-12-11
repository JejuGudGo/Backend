package com.example.jejugudgo.domain.course.search.service;

import com.example.jejugudgo.domain.course.common.dto.MapCoordinate;
import com.example.jejugudgo.domain.course.common.dto.RoutePoint;
import com.example.jejugudgo.domain.course.common.entity.OlleCourse;
import com.example.jejugudgo.domain.course.common.entity.OlleCourseLineData;
import com.example.jejugudgo.domain.course.common.entity.OlleSpot;
import com.example.jejugudgo.domain.course.common.enums.CourseType;
import com.example.jejugudgo.domain.course.common.repository.OlleCourseLineDataRepository;
import com.example.jejugudgo.domain.course.common.repository.OlleCourseRepository;
import com.example.jejugudgo.domain.course.common.repository.OlleSpotRepository;
import com.example.jejugudgo.domain.course.search.dto.request.CourseRequest;
import com.example.jejugudgo.domain.course.search.dto.response.CoursePathResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoursePathService {
    private final OlleCourseRepository olleCourseRepository;
    private final OlleSpotRepository olleSpotRepository;
    private final OlleCourseLineDataRepository olleCourseLineDataRepository;

    private static final CourseType JEJU_GUDGO = CourseType.COURSE_TYPE01;
    private static final CourseType OLLE = CourseType.COURSE_TYPE02;

    public CoursePathResponse getCoursePath(CourseRequest request) {
        if (request.cat1().equals(JEJU_GUDGO))
            return getJejuGudgoCoursePath(request);

        else if (request.cat1().equals(OLLE))
            return getOlleCoursePath(request);

        return null;
    }

    private CoursePathResponse getOlleCoursePath(CourseRequest request) {
        OlleCourse olleCourse = olleCourseRepository.findById(request.id())
                .orElse(null);

        if (olleCourse != null) {
            List<OlleSpot> olleSpots = olleSpotRepository.findByOlleCourseOrderBySpotOrderAsc(olleCourse);
            List<OlleCourseLineData> linePoints = olleCourseLineDataRepository.findByOlleCourseOrderByDataOrderAsc(olleCourse);

            List<RoutePoint> spots = olleSpots.stream()
                    .map(spot -> new RoutePoint(
                            spot.getTitle(),
                            spot.getLatitude(),
                            spot.getLongitude()
                    ))
                    .toList();

            List<MapCoordinate> points = linePoints.stream()
                    .map(point -> new MapCoordinate(
                            point.getLatitude(),
                            point.getLongitude()
                    ))
                    .toList();

            return new CoursePathResponse(
                    spots,
                    points
            );
        }
        return null;
    }

    // TODO: 지수님 코드 작성 후 생성하기
    private CoursePathResponse getJejuGudgoCoursePath(CourseRequest request) {
        return new CoursePathResponse(List.of(), List.of());
    }
}

package com.example.jejugudgo.domain.course.search.service;

import com.example.jejugudgo.domain.course.common.enums.CourseType;
import com.example.jejugudgo.domain.course.search.dto.request.CourseSearchRequest;
import com.example.jejugudgo.domain.course.search.dto.response.CourseSearchResponse;
import com.example.jejugudgo.domain.course.search.query.OlleTagSearchQueryService;
import com.example.jejugudgo.domain.course.search.query.TrailTagSearchQueryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagSearchService {
    private final OlleTagSearchQueryService olleTagSearchQueryService;
    private final TrailTagSearchQueryService trailTagSearchQueryService;

    private final String JEJU_GUDGO = CourseType.COURSE_TYPE01.getType();
    private final String OLLE = CourseType.COURSE_TYPE02.getType();
    private final String TRAIL = CourseType.COURSE_TYPE03.getType();
    private final String ALL = CourseType.COURSE_TYPE04.getType();


    public List<CourseSearchResponse> getCourses(HttpServletRequest httpRequest, CourseSearchRequest request) {
        if (request.cat1().equals(ALL))
            return getAllCourses(httpRequest, request);

        else if (request.cat1().equals(OLLE))
            return olleTagSearchQueryService.getCourses(httpRequest, request);

        else if (request.cat1().equals(TRAIL))
            return trailTagSearchQueryService.getCourses(httpRequest, request);

        return List.of();
    }

    private List<CourseSearchResponse> getAllCourses(HttpServletRequest httpRequest, CourseSearchRequest request) {
        List<CourseSearchResponse> allResponses = new ArrayList<>();

        List<CourseSearchResponse> olleCourses = olleTagSearchQueryService.getCourses(httpRequest, request);
        List<CourseSearchResponse> trailCourses = trailTagSearchQueryService.getCourses(httpRequest, request);

        allResponses.addAll(olleCourses);
        allResponses.addAll(trailCourses);

        sortCourses(allResponses);

        return allResponses;
    }

    private void sortCourses(List<CourseSearchResponse> courses) {
        courses.sort((a, b) -> {
            int comparison = compareNullable(b.reviewCount(), a.reviewCount());
            if (comparison != 0) return comparison;

            comparison = compareNullable(b.starAvg(), a.starAvg());
            if (comparison != 0) return comparison;

            comparison = compareNullable(b.likeCount(), a.likeCount());
            if (comparison != 0) return comparison;

            comparison = compareNullable(b.clickCount(), a.clickCount());
            if (comparison != 0) return comparison;

            return compareNullable(b.upToDate(), a.upToDate());
        });
    }

    private <T extends Comparable<T>> int compareNullable(T o1, T o2) {
        if (o1 == null && o2 == null) return 0;
        if (o1 == null) return 1;
        if (o2 == null) return -1;
        return o1.compareTo(o2);
    }
}

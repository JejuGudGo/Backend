package com.example.jejugudgo.domain.course.search.service;

import com.example.jejugudgo.domain.course.common.enums.CourseType;
import com.example.jejugudgo.domain.course.search.dto.request.CourseSearchRequest;
import com.example.jejugudgo.domain.course.search.dto.response.CourseSearchResponse;
import com.example.jejugudgo.domain.course.search.query.OlleTagSearchService;
import com.example.jejugudgo.domain.course.search.query.TrailTagSearchService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagSearchService implements SearchService {
    private final OlleTagSearchService olleTagSearchService;
    private final TrailTagSearchService trailTagSearchService;

    private final String JEJU_GUDGO = CourseType.COURSE_TYPE01.getType();
    private final String OLLE = CourseType.COURSE_TYPE02.getType();
    private final String TRAIL = CourseType.COURSE_TYPE03.getType();
    private final String ALL = CourseType.COURSE_TYPE04.getType();


    @Override
    public List<CourseSearchResponse> getCourses(HttpServletRequest httpRequest, CourseSearchRequest request) {
        if (request.cat1().equals(ALL))
            return getAllCourses(httpRequest, request);

        else if (request.cat1().equals(OLLE))
            return olleTagSearchService.getCourses(httpRequest, request);

        else if (request.cat1().equals(TRAIL))
            return trailTagSearchService.getCourses(httpRequest, request);

        return List.of();
    }

    private List<CourseSearchResponse> getAllCourses(HttpServletRequest httpRequest, CourseSearchRequest request) {
        List<CourseSearchResponse> allResponses = new ArrayList<>();

        List<CourseSearchResponse> olleCourses = olleTagSearchService.getCourses(httpRequest, request);

        allResponses.addAll(olleCourses);

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

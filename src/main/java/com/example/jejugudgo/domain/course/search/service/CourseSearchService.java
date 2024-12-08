package com.example.jejugudgo.domain.course.search.service;

import com.example.jejugudgo.domain.course.common.dto.MapCoordinate;
import com.example.jejugudgo.domain.course.search.dto.request.CourseSearchRequest;
import com.example.jejugudgo.domain.course.search.dto.response.CourseSearchResponse;
import com.example.jejugudgo.domain.course.search.elastic.query.TextSearchService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseSearchService {
    private final TagSearchService tagSearchService;
    private final TextSearchService textSearchService;

    public List<CourseSearchResponse> getCourses(HttpServletRequest httpRequest, String keyword, String cat1, List<String> cat2, List<String> cat3, List<String> coordinates) {
        CourseSearchRequest request = setCourseSearchRequest(keyword, cat1, cat2, cat3, coordinates);
        if (request.keyword() != null)
            return textSearchService.getCourses(httpRequest, request);

        return tagSearchService.getCourses(httpRequest, request);
    }

    private CourseSearchRequest setCourseSearchRequest(String keyword, String cat1, List<String> cat2, List<String> cat3, List<String> coordinates) {
        List<MapCoordinate> mapCoordinates = new ArrayList<>();

        double minLatitude = Math.min(Double.parseDouble(coordinates.get(0)), Double.parseDouble(coordinates.get(2)));
        double maxLatitude = Math.max(Double.parseDouble(coordinates.get(0)), Double.parseDouble(coordinates.get(2)));
        double minLongitude = Math.min(Double.parseDouble(coordinates.get(1)), Double.parseDouble(coordinates.get(3)));
        double maxLongitude = Math.max(Double.parseDouble(coordinates.get(1)), Double.parseDouble(coordinates.get(3)));

        MapCoordinate minCoordinate = new MapCoordinate(minLatitude, minLongitude);
        MapCoordinate maxCoordinate = new MapCoordinate(maxLatitude, maxLongitude);

        mapCoordinates.add(minCoordinate);
        mapCoordinates.add(maxCoordinate);

        return new CourseSearchRequest(
                keyword,
                cat1,
                cat2,
                cat3,
                mapCoordinates
        );
    }
}

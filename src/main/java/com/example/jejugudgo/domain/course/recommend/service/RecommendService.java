package com.example.jejugudgo.domain.course.recommend.service;

import com.example.jejugudgo.domain.course.recommend.dto.OpenApiRequest;
import com.example.jejugudgo.domain.course.recommend.dto.OpenApiResponse;
import com.example.jejugudgo.domain.course.recommend.enums.ContentType;
import com.example.jejugudgo.domain.course.recommend.enums.RecommendType;
import com.example.jejugudgo.domain.course.recommend.query.ConvenienceStoreQueryService;
import com.example.jejugudgo.domain.course.recommend.query.TourApiQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendService {
    private final TourApiQueryService tourApiQueryService;
    private final ConvenienceStoreQueryService convenienceStoreQueryService;

    private static final String CONVENIECE = RecommendType.RECOMMEND_TYPE04.getType();

    public List<OpenApiResponse> getRecommendations(String type, List<String> coordinates, String distance) {
        OpenApiRequest request = getOpenApiRequest(type, coordinates, distance);

        if (type.equals(CONVENIECE)) {
            return convenienceStoreQueryService.getContents(request);
        }

        return tourApiQueryService.getContents(request);
    }

    private OpenApiRequest getOpenApiRequest(String type, List<String> coordinates, String radius) {
        Double latitude = null;
        Double longitude = null;
        Double radiusValue = null;

        if (coordinates != null && !coordinates.isEmpty()) {
            latitude = Double.parseDouble(coordinates.get(0));
            longitude = Double.parseDouble(coordinates.get(1));
            radiusValue = Double.parseDouble(radius);
        }

        if (type.equals(CONVENIECE)) {
            return new OpenApiRequest(
                    null,
                    latitude,
                    longitude,
                    radiusValue
            );
        }

        return new OpenApiRequest(
                getContentType(type),
                latitude,
                longitude,
                radiusValue
        );
    }

    private ContentType getContentType(String contentTypeName) {
        return ContentType.fromContentTypeName(contentTypeName);
    }
}

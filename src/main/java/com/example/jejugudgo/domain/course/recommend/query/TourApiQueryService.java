package com.example.jejugudgo.domain.course.recommend.query;

import com.example.jejugudgo.domain.course.recommend.dto.OpenApiRequest;
import com.example.jejugudgo.domain.course.recommend.dto.OpenApiResponse;
import com.example.jejugudgo.domain.course.recommend.entity.QTourApiSpot;
import com.example.jejugudgo.domain.course.recommend.entity.TourApiSpot;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TourApiQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public TourApiQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    QTourApiSpot tourApiSpot = QTourApiSpot.tourApiSpot;

    public List<OpenApiResponse> getContents(OpenApiRequest request) {
        System.out.println("request type: " + request.type());
        JPQLQuery<TourApiSpot> query = queryFactory
                .selectFrom(tourApiSpot)
                .where(
                        tourApiSpot.contentType.contentType.eq(request.type())
                )
                .orderBy(
                        tourApiSpot.updatedAt.desc(),
                        tourApiSpot.viewCount.desc()
                );

        if (request.latitude() != null && request.longitude() != null && request.radius() != null) {
            query
                    .where(
                            isWithinRadiusCondition(request.latitude(), request.longitude(), request.radius())
                    );
        }

        return query.fetch()
                .stream()
                .map(spot -> new OpenApiResponse(
                        spot.getThumbnailUrl(),
                        spot.getTitle(),
                        spot.getAddress(),
                        spot.getLatitude(),
                        spot.getLongitude()
                ))
                .toList();
    }

    private BooleanExpression isWithinRadiusCondition(double latitude, double longitude, double radius) {
        return Expressions.booleanTemplate(
                "6371 * acos(cos(radians({0})) * cos(radians({1})) * cos(radians({2}) - radians({3})) + sin(radians({0})) * sin(radians({1}))) <= {4}",
                latitude,
                tourApiSpot.latitude,
                tourApiSpot.longitude,
                longitude,
                radius
        );
    }
}

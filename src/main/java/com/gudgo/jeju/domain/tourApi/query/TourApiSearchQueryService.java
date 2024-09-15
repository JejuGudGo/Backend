package com.gudgo.jeju.domain.tourApi.query;

import com.gudgo.jeju.domain.tourApi.entity.QTourApiContent;
import com.gudgo.jeju.domain.tourApi.entity.TourApiContent;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TourApiSearchQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public TourApiSearchQueryService(EntityManager entityManager) {
    this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<TourApiContent> searchTourApiSpots(double latitude, double longitude, double radiusKm, String contentTypeId) {
        QTourApiContent tourApiContent = QTourApiContent.tourApiContent;

        double latitudeDelta = radiusKm / 111.0;
        double longitudeDelta = radiusKm / (111.0 * Math.cos(Math.toRadians(latitude)));

        double minLat = latitude - latitudeDelta;
        double maxLat = latitude + latitudeDelta;
        double minLng = longitude - longitudeDelta;
        double maxLng = longitude + longitudeDelta;

        double finalMinLat = Math.min(minLat, maxLat);
        double finalMaxLat = Math.max(minLat, maxLat);
        double finalMinLng = Math.min(minLng, maxLng);
        double finalMaxLng = Math.max(minLng, maxLng);

        log.debug("Final MinLat: {}, Final MaxLat: {}", finalMinLat, finalMaxLat);
        log.debug("Final MinLng: {}, Final MaxLng: {}", finalMinLng, finalMaxLng);

        log.info("minLat: " +  minLat + "maxLat: " + maxLat +  "minLng: " + minLng +  "maxLng: " + maxLng);

        return queryFactory.selectFrom(tourApiContent)
                .where(tourApiContent.latitude.between(finalMinLat, finalMaxLat)
                        .and(tourApiContent.longitude.between(finalMinLng, finalMaxLng))
                        .and(tourApiContent.contentTypeId.eq(contentTypeId)))
                .fetch();
    }
}

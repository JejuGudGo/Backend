package com.example.jejugudgo.global.api.tourapi.area.query;

import com.example.jejugudgo.global.api.tourapi.area.dto.TourApiSpotListResponse;
import com.example.jejugudgo.global.api.tourapi.common.entity.ContentType;
import com.example.jejugudgo.global.api.tourapi.common.entity.QTourApiSpots;
import com.example.jejugudgo.global.exception.CustomException;
import com.example.jejugudgo.global.exception.entity.RetCode;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TourApiSpotQueryService {
    private final JPAQueryFactory queryFactory;

    public TourApiSpotQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Page<TourApiSpotListResponse> getAllTourApiSpotsByContentType(ContentType contentType, Pageable pageable) {
        QTourApiSpots tourApiSpots = QTourApiSpots.tourApiSpots;

        List<TourApiSpotListResponse> spots = queryFactory
                .selectFrom(tourApiSpots)
                .where(eqContentType(tourApiSpots, contentType))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(spot -> new TourApiSpotListResponse(
                        spot.getId(),
                        spot.getTitle(),
                        spot.getSummary(),
                        spot.getAddress()
                ))
                .collect(Collectors.toList());

        // 전체 개수 조회 대체 코드
        long total = Optional.ofNullable(
                queryFactory
                        .select(tourApiSpots.count())
                        .from(tourApiSpots)
                        .where(eqContentType(tourApiSpots, contentType))
                        .fetchOne()
        ).orElse(0L);

        if (total == 0) {
            throw new CustomException(RetCode.RET_CODE97);
        }

        return new PageImpl<>(spots, pageable, total);
    }

    private BooleanExpression eqContentType(QTourApiSpots tourApiSpots, ContentType contentType) {
        return tourApiSpots.tourApiContentType.contentType.eq(contentType);
    }
}

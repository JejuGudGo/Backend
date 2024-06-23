package com.gudgo.jeju.domain.olle.query;


import com.gudgo.jeju.domain.olle.dto.response.OlleCourseDetailResponseDto;
import com.gudgo.jeju.domain.olle.dto.response.OlleCourseResponseDto;
import com.gudgo.jeju.domain.olle.dto.response.OlleCourseSpotResponseDto;
import com.gudgo.jeju.domain.olle.entity.JeJuOlleCourse;
import com.gudgo.jeju.domain.olle.entity.JeJuOlleSpot;
import com.gudgo.jeju.domain.olle.entity.QJeJuOlleCourse;
import com.gudgo.jeju.domain.olle.entity.QJeJuOlleSpot;
import com.gudgo.jeju.global.util.PaginationUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OlleCourseQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public OlleCourseQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Page<OlleCourseResponseDto> getOlleCourses(Pageable pageable) {
        QJeJuOlleCourse qJeJuOlleCourse = QJeJuOlleCourse.jeJuOlleCourse;

        List<JeJuOlleCourse> jeJuOlleCourses = queryFactory
                .selectFrom(qJeJuOlleCourse)
                .fetch();

        List<OlleCourseResponseDto> courseResponseDtos = jeJuOlleCourses.stream()
                .map(olleCourse ->
                        new OlleCourseResponseDto(
                                olleCourse.getId(),
                                olleCourse.getOlleType(),
                                olleCourse.getTitle(),
                                olleCourse.getStartLatitude(),
                                olleCourse.getStartLongitude(),
                                olleCourse.getEndLatitude(),
                                olleCourse.getEndLongitude(),
                                olleCourse.isWheelchairAccessible(),
                                olleCourse.getTotalDistance(),
                                olleCourse.getTotalTime()
                        ))
                .toList();
        return PaginationUtil.listToPage(courseResponseDtos, pageable);
    }

    public OlleCourseDetailResponseDto getOlleCourse(Long olleId) {
        QJeJuOlleSpot qJeJuOlleSpot = QJeJuOlleSpot.jeJuOlleSpot;
        QJeJuOlleCourse qJeJuOlleCourse = QJeJuOlleCourse.jeJuOlleCourse;

        JeJuOlleCourse jeJuOlleCourse = queryFactory
                .selectFrom(qJeJuOlleCourse)
                .where(qJeJuOlleCourse.id.eq(olleId))
                .fetchOne();

        List<JeJuOlleSpot> jeJuOlleSpots = queryFactory
                .selectFrom(qJeJuOlleSpot)
                .where(qJeJuOlleSpot.jeJuOlleCourse.id.eq(olleId))
                .orderBy(qJeJuOlleSpot.orderNumber.asc())
                .fetch();

        List<OlleCourseSpotResponseDto> spots = jeJuOlleSpots.stream()
                .map(spot ->
                        new OlleCourseSpotResponseDto(
                                spot.getTitle(),
                                spot.getLatitude(),
                                spot.getLongitude(),
                                spot.getDistance()
                        )
                ).toList();

        OlleCourseDetailResponseDto response = new OlleCourseDetailResponseDto(
                olleId,
                jeJuOlleCourse.getOlleType(),
                jeJuOlleCourse.getCourseNumber(),
                jeJuOlleCourse.getTitle(),
                jeJuOlleCourse.getStartLatitude(),
                jeJuOlleCourse.getStartLongitude(),
                jeJuOlleCourse.getEndLatitude(),
                jeJuOlleCourse.getEndLongitude(),
                spots
        );

        return response;
    }
}

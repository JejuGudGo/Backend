package com.example.jejugudgo.domain.olle.query;

import com.example.jejugudgo.domain.olle.dto.response.JejuOlleCourseResponseDetail;
import com.example.jejugudgo.domain.olle.dto.response.JejuOlleCourseResponseForList;
import com.example.jejugudgo.domain.olle.dto.response.JejuOlleSpotResponse;
import com.example.jejugudgo.domain.olle.entity.JejuOlleCourse;
import com.example.jejugudgo.domain.olle.entity.JejuOlleSpot;
import com.example.jejugudgo.domain.olle.entity.QJejuOlleCourse;
import com.example.jejugudgo.domain.olle.entity.QJejuOlleSpot;
import com.example.jejugudgo.global.util.PaginationUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JejuOlleCourseQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public JejuOlleCourseQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }


    public Page<JejuOlleCourseResponseForList> getOlleCourses(Pageable pageable) {
        QJejuOlleCourse qJejuOlleCourse = QJejuOlleCourse.jejuOlleCourse;

        List<JejuOlleCourse> jejuOlleCourseList = queryFactory
                .selectFrom(qJejuOlleCourse)
                .fetch();

        List<JejuOlleCourseResponseForList> courseResponseForLists = jejuOlleCourseList.stream()
                .map(jejuOlleCourse ->
                        new JejuOlleCourseResponseForList(
                                jejuOlleCourse.getId(),
                                jejuOlleCourse.getTitle(),
                                jejuOlleCourse.getStartSpotTitle(),
                                jejuOlleCourse.getEndSpotTitle(),
                                jejuOlleCourse.getTotalTime(),
                                jejuOlleCourse.getStarAvg(),
                                0L
                        ))
                .toList();
        return PaginationUtil.listToPage(courseResponseForLists, pageable);
    }

    public JejuOlleCourseResponseDetail getOlleCourse(Long courseId) {
        QJejuOlleCourse qJejuOlleCourse = QJejuOlleCourse.jejuOlleCourse;
        QJejuOlleSpot qJejuOlleSpot = QJejuOlleSpot.jejuOlleSpot;

        JejuOlleCourse jeJuOlleCourse = queryFactory
                .selectFrom(qJejuOlleCourse)
                .where(qJejuOlleCourse.id.eq(courseId))
                .fetchOne();

        List<JejuOlleSpot> jeJuOlleSpots = queryFactory
                .selectFrom(qJejuOlleSpot)
                .where(qJejuOlleSpot.jejuOlleCourse.id.eq(courseId))
                .orderBy(qJejuOlleSpot.spotOrder.asc())
                .fetch();

        List<JejuOlleSpotResponse> spotResponses = jeJuOlleSpots.stream()
                .map(spot ->
                        new JejuOlleSpotResponse(
                                spot.getId(),
                                spot.getTitle(),
                                spot.getLatitude(),
                                spot.getLongitude(),
                                spot.getSpotOrder()
                        )).toList();

        return new JejuOlleCourseResponseDetail(
                courseId,
                jeJuOlleCourse.getTitle(),
                jeJuOlleCourse.getStartSpotTitle(),
                jeJuOlleCourse.getEndSpotTitle(),
                jeJuOlleCourse.getTotalTime(),
                jeJuOlleCourse.getStarAvg(),
                0L,
                spotResponses,
                jeJuOlleCourse.getSummary(),
                jeJuOlleCourse.getInfoAddress(),
                jeJuOlleCourse.getInfoOpenTime(),
                jeJuOlleCourse.getInfoPhone(),
                jeJuOlleCourse.getCourseImageUrl()
        );
    }

}


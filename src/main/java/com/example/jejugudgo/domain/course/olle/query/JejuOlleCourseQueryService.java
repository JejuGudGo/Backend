package com.example.jejugudgo.domain.course.olle.query;

import com.example.jejugudgo.domain.course.olle.dto.response.JejuOlleCourseResponseDetail;
import com.example.jejugudgo.domain.course.olle.dto.response.JejuOlleCourseResponseForList;
import com.example.jejugudgo.domain.course.olle.dto.response.JejuOlleSpotResponse;
import com.example.jejugudgo.domain.course.olle.entity.JejuOlleCourse;
import com.example.jejugudgo.domain.course.olle.entity.JejuOlleSpot;
import com.example.jejugudgo.domain.course.olle.entity.QJejuOlleCourse;
import com.example.jejugudgo.domain.course.olle.entity.QJejuOlleSpot;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.util.PagingUtil;
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


    public List<JejuOlleCourseResponseForList> getOlleCourses(Pageable pageable) {
        QJejuOlleCourse qJejuOlleCourse = QJejuOlleCourse.jejuOlleCourse;

        List<JejuOlleCourse> jejuOlleCourseList = queryFactory
                .selectFrom(qJejuOlleCourse)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (jejuOlleCourseList.isEmpty()) {
            throw new CustomException(RetCode.RET_CODE97);
        }

        List<JejuOlleCourseResponseForList> courseResponseForLists = jejuOlleCourseList.stream()
                .map(jejuOlleCourse ->
                        new JejuOlleCourseResponseForList(
                                jejuOlleCourse.getId(),
                                jejuOlleCourse.getTitle(),
                                jejuOlleCourse.getStartSpotTitle(),
                                jejuOlleCourse.getEndSpotTitle(),
                                jejuOlleCourse.getTime(),
                                jejuOlleCourse.getStarAvg(),
                                0
                        ))
                .toList();
        return courseResponseForLists;
    }

    public JejuOlleCourseResponseDetail getOlleCourse(Long courseId) {
        QJejuOlleCourse qJejuOlleCourse = QJejuOlleCourse.jejuOlleCourse;
        QJejuOlleSpot qJejuOlleSpot = QJejuOlleSpot.jejuOlleSpot;

        JejuOlleCourse jeJuOlleCourse = queryFactory
                .selectFrom(qJejuOlleCourse)
                .where(qJejuOlleCourse.id.eq(courseId))
                .fetchOne();

        if (jeJuOlleCourse == null) {
            throw new CustomException(RetCode.RET_CODE97);
        }

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
                jeJuOlleCourse.getTime(),
                jeJuOlleCourse.getStarAvg(),
                0,
                spotResponses,
                jeJuOlleCourse.getSummary(),
                jeJuOlleCourse.getInfoAddress(),
                jeJuOlleCourse.getInfoOpenTime(),
                jeJuOlleCourse.getInfoPhone(),
                jeJuOlleCourse.getCourseImageUrl()
        );
    }

}


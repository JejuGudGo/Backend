package com.gudgo.jeju.domain.planner.spot.query;

import com.gudgo.jeju.domain.planner.planner.entity.QPlanner;
import com.gudgo.jeju.domain.planner.spot.entity.QSpot;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class SpotQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public SpotQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Long getLastSpotId(Long courseId) {
        QSpot qSpot = QSpot.spot;

        Long lastSpotId = queryFactory
                .select(qSpot.id)
                .from(qSpot)
                .where(qSpot.course.id.eq(courseId)
                        .and(qSpot.isDeleted.isFalse()))
                .orderBy(qSpot.orderNumber.desc())
                .limit(1)
                .fetchOne();

        return lastSpotId;
    }
}

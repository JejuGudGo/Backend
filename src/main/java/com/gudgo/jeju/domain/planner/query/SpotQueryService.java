package com.gudgo.jeju.domain.planner.query;

import com.gudgo.jeju.domain.planner.entity.QSpot;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
                .orderBy(qSpot.count.desc())
                .limit(1)
                .fetchOne();

        return lastSpotId;
    }
}

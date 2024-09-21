package com.gudgo.jeju.domain.event.query;

import com.gudgo.jeju.domain.event.dto.response.EventResponse;
import com.gudgo.jeju.domain.event.entity.Event;
import com.gudgo.jeju.domain.event.entity.EventType;
import com.gudgo.jeju.domain.event.entity.QEvent;
import com.gudgo.jeju.global.util.PaginationUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import ognl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired EventQueryService(EntityManager entityManager){
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Page<EventResponse> getEvents(EventType type, Pageable pageable) {
        QEvent event = QEvent.event;

        List<Event> events = queryFactory
                .selectFrom(event)
                .where(event.type.eq(type))
                .fetch();

        List<EventResponse> eventResponses = events.stream().map(e -> {
            return new EventResponse(
                    e.getId(),
                    e.getTitle(),
                    e.getOrganization(),
                    e.getStartDate(),
                    e.getFinishDate(),
                    e.getImageUrl(),
                    e.getInformationUrl(),
                    e.getType()
            );
        }).collect(Collectors.toList());

        return PaginationUtil.listToPage(eventResponses, pageable);
    }



}

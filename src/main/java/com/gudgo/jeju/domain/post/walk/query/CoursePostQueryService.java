package com.gudgo.jeju.domain.post.walk.query;

import com.gudgo.jeju.domain.post.common.entity.PostType;
import com.gudgo.jeju.domain.post.common.entity.Posts;
import com.gudgo.jeju.domain.post.common.entity.QPosts;
import com.gudgo.jeju.domain.post.participant.entity.QParticipant;
import com.gudgo.jeju.domain.post.walk.dto.response.CoursePostListResponse;
import com.gudgo.jeju.global.util.PaginationUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoursePostQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public CoursePostQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Page<CoursePostListResponse> getAllCoursePosts(String query, Pageable pageable) {
        QPosts qPosts = QPosts.posts;

        List<Posts> posts = new ArrayList<>();

        if (query.equals("ALL")) {
            posts = queryFactory
                    .selectFrom(qPosts)
                    .where(qPosts.isDeleted.isFalse())
                    .fetch();

        } else if (query.equals("OLLE")) {
            posts = queryFactory
                    .selectFrom(qPosts)
                    .where(qPosts.isDeleted.isFalse()
                            .and(qPosts.planner.course.olleCourseId.isNotNull()))
                    .fetch();

        } else if (query.equals("USER")) {
            posts = queryFactory
                    .selectFrom(qPosts)
                    .where(qPosts.isDeleted.isFalse()
                            .and(qPosts.planner.course.olleCourseId.isNull()))
                    .fetch();
        }

        List<CoursePostListResponse> coursePostCreateResponse = posts.stream()
                .map(post ->
                        new CoursePostListResponse(
                                post.getId(),
                                getStatus(post.getStartDate(), post.getStartAt()),
                                post.getTitle(),
                                post.getPlanner().getCourse().getImageUrl(),
                                post.getCourseSummary(),
                                post.getStartDate(),
                                post.getStartAt(),
                                post.getCreatedAt(),
                                getCurrentParticipantNum(post.getId()),
                                post.getParticipantNum()
                        ))
                .toList();

        return PaginationUtil.listToPage(coursePostCreateResponse, pageable);
    }

    private Long getCurrentParticipantNum(Long plannerId) {
        QParticipant qParticipant = QParticipant.participant;

        Long currentParticipantNum = queryFactory
                .select(qParticipant.count())
                .from(qParticipant)
                .where((qParticipant.planner.id.eq(plannerId)
                        .and(qParticipant.approved.isTrue())
                        .and(qParticipant.isDeleted.isFalse())))
                .fetchOne();

        return currentParticipantNum;
    }

    private String getStatus(LocalDate startDate, LocalTime startAt) {
        LocalDateTime currentDateTime = LocalDateTime.now();

        LocalDateTime eventDateTime = LocalDateTime.of(startDate, startAt);

        if (currentDateTime.isAfter(eventDateTime)) return "CLOSE";
        else return "OPEN";
    }
}

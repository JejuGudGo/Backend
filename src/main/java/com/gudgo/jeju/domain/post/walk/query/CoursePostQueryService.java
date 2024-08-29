package com.gudgo.jeju.domain.post.walk.query;

import com.gudgo.jeju.domain.planner.entity.QParticipant;
import com.gudgo.jeju.domain.post.walk.dto.response.CoursePostResponse;
import com.gudgo.jeju.domain.post.common.entity.PostType;
import com.gudgo.jeju.domain.post.common.entity.Posts;
import com.gudgo.jeju.domain.post.entity.QPosts;
import com.gudgo.jeju.global.util.PaginationUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoursePostQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public CoursePostQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Page<CoursePostResponse> getCoursePosts(Pageable pageable) {
        QPosts qPosts = QPosts.posts;

        List<Posts> posts = queryFactory
                .selectFrom(qPosts)
                .where(qPosts.isDeleted.isFalse()
                        .and(qPosts.isFinished.isFalse())
                        .and(qPosts.postType.eq(PostType.COURSE)))
                .fetch();

        List<CoursePostResponse> coursePostResponses = posts.stream()
                .map(post ->
                        new CoursePostResponse(
                                post.getId(),
                                post.getUser().getId(),
                                post.getUser().getNickname(),
                                post.getUser().getProfile().getProfileImageUrl(),
                                post.getUser().getNumberTag(),
                                post.getTitle(),
                                post.getCompanionsNum(),
                                getCurrentParticipantNum(post.getId()),
                                post.getContent()
                        ))
                .toList();

        return PaginationUtil.listToPage(coursePostResponses, pageable);
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
}

package com.gudgo.jeju.domain.post.walk.query;

import com.gudgo.jeju.domain.post.participant.entity.QParticipant;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoursePostQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public CoursePostQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

//    public Page<CoursePostCreateResponse> getCoursePosts(Pageable pageable) {
//        QPosts qPosts = QPosts.posts;
//
//        List<Posts> posts = queryFactory
//                .selectFrom(qPosts)
//                .where(qPosts.isDeleted.isFalse()
//                        .and(qPosts.isFinished.isFalse())
//                        .and(qPosts.postType.eq(PostType.COURSE)))
//                .fetch();
//
//        List<CoursePostCreateResponse> coursePostCreateRespons = posts.stream()
//                .map(post ->
//                        new CoursePostCreateResponse(
//                                post.getId(),
//                                post.getUser().getId(),
//                                post.getUser().getNickname(),
//                                post.getUser().getProfile().getProfileImageUrl(),
//                                post.getUser().getNumberTag(),
//                                post.getTitle(),
//                                post.getCompanionsNum(),
//                                getCurrentParticipantNum(post.getId()),
//                                post.getContent()
//                        ))
//                .toList();
//
//        return PaginationUtil.listToPage(coursePostCreateRespons, pageable);
//    }

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

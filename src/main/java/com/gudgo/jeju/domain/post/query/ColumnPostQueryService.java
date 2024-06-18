package com.gudgo.jeju.domain.post.query;

import com.gudgo.jeju.domain.post.dto.response.ColumnPostResponse;
import com.gudgo.jeju.domain.post.entity.PostType;
import com.gudgo.jeju.domain.post.entity.Posts;
import com.gudgo.jeju.domain.post.entity.QPosts;
import com.gudgo.jeju.domain.user.entity.QUser;
import com.gudgo.jeju.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColumnPostQueryService {
    private final JPAQueryFactory queryFactory;
    private final ImageQueryService imageQueryService;
    private final CommentQueryService commentQueryService;

    @Autowired
    public ColumnPostQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.imageQueryService = new ImageQueryService(entityManager);
        this.commentQueryService = new CommentQueryService(entityManager);
    }

    public List<ColumnPostResponse> getColums() {
        QPosts qPosts = QPosts.posts;

        List<Posts> posts = queryFactory
                .selectFrom(qPosts)
                .where(qPosts.postType.eq(PostType.COLUMN)
                        .and(qPosts.isDeleted.isFalse()))
                .fetch();

        List<ColumnPostResponse> columnPostResponses = posts.stream()
                .map(post -> {
                    User user = findUserById(post.getUser().getId());
                    String profileImageUrl = "";

                    if (!user.getProfile().getProfileImageUrl().isEmpty()) {
                        profileImageUrl = user.getProfile().getProfileImageUrl();
                    }

                    return new ColumnPostResponse(
                            post.getId(),
                            user.getId(),
                            user.getNickname(),
                            profileImageUrl,
                            user.getNumberTag(),
                            post.getTitle(),
                            post.getContent()
                    );
                })
                .toList();

        return columnPostResponses;
    }

    private User findUserById(Long userId) {
        QUser qUser = QUser.user;

        User user = queryFactory
                .selectFrom(qUser)
                .where(qUser.id.eq(userId))
                .fetchOne();

        return user;
    }
}

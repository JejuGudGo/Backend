package com.gudgo.jeju.domain.post.column.query;

import com.gudgo.jeju.domain.post.column.dto.response.ColumnPostResponse;
import com.gudgo.jeju.domain.post.common.entity.PostType;
import com.gudgo.jeju.domain.post.common.entity.Posts;
import com.gudgo.jeju.domain.post.common.entity.QPosts;
import com.gudgo.jeju.domain.user.entity.QUser;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.global.util.PaginationUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColumnPostQueryService {
    private final JPAQueryFactory queryFactory;
    private final PostImageQueryService postImageQueryService;
    private final CommentQueryService commentQueryService;

    @Autowired
    public ColumnPostQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.postImageQueryService = new PostImageQueryService(entityManager);
        this.commentQueryService = new CommentQueryService(entityManager);
    }

    public Page<ColumnPostResponse> getColums(Pageable pageable) {
        QPosts qPosts = QPosts.posts;

        List<Posts> posts = queryFactory
                .selectFrom(qPosts)
                .where(qPosts.postType.eq(PostType.COLUMN)
                        .and(qPosts.isDeleted.isFalse()))
                .fetch();

        List<ColumnPostResponse> columnPostResponses = posts.stream()
                .map(post -> {
                    User user = findUserById(post.getUser().getId());
                    String profileImageUrl = "Default";

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
                            post.getContent(),
                            postImageQueryService.getPostImages(post.getId())
                    );
                })
                .toList();

        return PaginationUtil.listToPage(columnPostResponses, pageable);
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

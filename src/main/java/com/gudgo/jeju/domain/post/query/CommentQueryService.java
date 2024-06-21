package com.gudgo.jeju.domain.post.query;

import com.gudgo.jeju.domain.post.dto.response.CommentResponse;
import com.gudgo.jeju.domain.post.dto.response.NestedCommentResponse;
import com.gudgo.jeju.domain.post.entity.Comment;
import com.gudgo.jeju.domain.post.entity.QComment;
import com.gudgo.jeju.domain.user.entity.QUser;
import com.gudgo.jeju.domain.user.entity.User;
import com.gudgo.jeju.global.util.PaginationUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public CommentQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Page<CommentResponse> getPostImages(Long postId, Pageable pageable) {
        QComment qComment = QComment.comment;

        List<Comment> comments = queryFactory
                .selectFrom(qComment)
                .where(qComment.posts.id.eq(postId)
                        .and(qComment.isDeleted.isFalse()))
                .fetch();

        List<CommentResponse> commentResponses = comments.stream()
                .map(comment -> {
                    User user = findUserById(comment.getUserId());
                    String profileImageUrl = "";

                    if (!user.getProfile().getProfileImageUrl().isEmpty()) {
                        profileImageUrl = user.getProfile().getProfileImageUrl();
                    }

                    return new CommentResponse(
                            comment.getId(),
                            user.getId(),
                            user.getNickname(),
                            profileImageUrl,
                            user.getNumberTag(),
                            comment.getContent()
                    );
                })
                .toList();

        return PaginationUtil.listToPage(commentResponses, pageable);
    }

    public Page<NestedCommentResponse> getNestedComments(Long commentId, Pageable pageable) {
        QComment qComment = QComment.comment;

        List<Comment> nestedComments = queryFactory
                .selectFrom(qComment)
                .where((qComment.parentCommentId.eq(commentId))
                        .and(qComment.isDeleted.isFalse()))
                .fetch();

        List<NestedCommentResponse> nestedCommentResponses = nestedComments.stream()
                .map(nestedComment -> {
                    User user = findUserById(nestedComment.getUserId());
                    String profileImageUrl = "";

                    if (!user.getProfile().getProfileImageUrl().isEmpty()) {
                        profileImageUrl = user.getProfile().getProfileImageUrl();
                    }

                    return new NestedCommentResponse(
                            nestedComment.getId(),
                            user.getId(),
                            user.getNickname(),
                            profileImageUrl,
                            user.getNumberTag(),
                            nestedComment.getContent()
                    );
                })
                .collect(Collectors.toList());

        return PaginationUtil.listToPage(nestedCommentResponses, pageable);
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
package com.gudgo.jeju.domain.post.column.query;

import com.gudgo.jeju.domain.post.column.dto.response.PostImageResponse;
import com.gudgo.jeju.domain.post.common.entity.PostImage;
import com.gudgo.jeju.domain.post.entity.QPostImage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostImageQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public PostImageQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<PostImageResponse> getPostImages(Long postId) {
        QPostImage qPostImage = QPostImage.postImage;

        List<PostImage> postImages = queryFactory
                .selectFrom(qPostImage)
                .where(qPostImage.posts.id.eq(postId)
                        .and(qPostImage.isDeleted.isFalse()))
                .fetch();

        List<PostImageResponse> postImageRepons = postImages.stream()
                .map(postImage -> new PostImageResponse(
                        postImage.getId(),
                        postImage.getImageUrl()
                ))
                .toList();

        return postImageRepons;
    }
}

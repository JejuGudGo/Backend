package com.gudgo.jeju.domain.post.query;

import com.gudgo.jeju.domain.post.dto.response.ColumnImageResponse;
import com.gudgo.jeju.domain.post.entity.PostImage;
import com.gudgo.jeju.domain.post.entity.QPostImage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageQueryService {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public ImageQueryService(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<ColumnImageResponse> getPostImages(Long postId) {
        QPostImage qPostImage = QPostImage.postImage;

        List<PostImage> postImages = queryFactory
                .selectFrom(qPostImage)
                .where(qPostImage.posts.id.eq(postId)
                        .and(qPostImage.isDeleted.isFalse()))
                .fetch();

        List<ColumnImageResponse> columnImageResponses = postImages.stream()
                .map(postImage -> new ColumnImageResponse(
                        postImage.getId(),
                        postImage.getImageUrl()
                ))
                .toList();

        return columnImageResponses;
    }
}

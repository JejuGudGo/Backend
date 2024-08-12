package com.gudgo.jeju.domain.review.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlannerReviewImage is a Querydsl query type for PlannerReviewImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlannerReviewImage extends EntityPathBase<PlannerReviewImage> {

    private static final long serialVersionUID = -1206385851L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlannerReviewImage plannerReviewImage = new QPlannerReviewImage("plannerReviewImage");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final QPlannerReview plannerReview;

    public QPlannerReviewImage(String variable) {
        this(PlannerReviewImage.class, forVariable(variable), INITS);
    }

    public QPlannerReviewImage(Path<? extends PlannerReviewImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlannerReviewImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlannerReviewImage(PathMetadata metadata, PathInits inits) {
        this(PlannerReviewImage.class, metadata, inits);
    }

    public QPlannerReviewImage(Class<? extends PlannerReviewImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.plannerReview = inits.isInitialized("plannerReview") ? new QPlannerReview(forProperty("plannerReview"), inits.get("plannerReview")) : null;
    }

}


package com.gudgo.jeju.domain.review.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlannerReviewCategory is a Querydsl query type for PlannerReviewCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlannerReviewCategory extends EntityPathBase<PlannerReviewCategory> {

    private static final long serialVersionUID = 1755652820L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlannerReviewCategory plannerReviewCategory = new QPlannerReviewCategory("plannerReviewCategory");

    public final StringPath code = createString("code");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QPlannerReview plannerReview;

    public QPlannerReviewCategory(String variable) {
        this(PlannerReviewCategory.class, forVariable(variable), INITS);
    }

    public QPlannerReviewCategory(Path<? extends PlannerReviewCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlannerReviewCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlannerReviewCategory(PathMetadata metadata, PathInits inits) {
        this(PlannerReviewCategory.class, metadata, inits);
    }

    public QPlannerReviewCategory(Class<? extends PlannerReviewCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.plannerReview = inits.isInitialized("plannerReview") ? new QPlannerReview(forProperty("plannerReview"), inits.get("plannerReview")) : null;
    }

}


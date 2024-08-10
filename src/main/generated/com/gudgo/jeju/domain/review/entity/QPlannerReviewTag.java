package com.gudgo.jeju.domain.review.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlannerReviewTag is a Querydsl query type for PlannerReviewTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlannerReviewTag extends EntityPathBase<PlannerReviewTag> {

    private static final long serialVersionUID = -559903740L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlannerReviewTag plannerReviewTag = new QPlannerReviewTag("plannerReviewTag");

    public final StringPath code = createString("code");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final QPlannerReviewCategory plannerReviewCategory;

    public QPlannerReviewTag(String variable) {
        this(PlannerReviewTag.class, forVariable(variable), INITS);
    }

    public QPlannerReviewTag(Path<? extends PlannerReviewTag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlannerReviewTag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlannerReviewTag(PathMetadata metadata, PathInits inits) {
        this(PlannerReviewTag.class, metadata, inits);
    }

    public QPlannerReviewTag(Class<? extends PlannerReviewTag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.plannerReviewCategory = inits.isInitialized("plannerReviewCategory") ? new QPlannerReviewCategory(forProperty("plannerReviewCategory"), inits.get("plannerReviewCategory")) : null;
    }

}


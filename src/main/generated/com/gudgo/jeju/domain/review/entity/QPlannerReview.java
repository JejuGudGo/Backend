package com.gudgo.jeju.domain.review.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlannerReview is a Querydsl query type for PlannerReview
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlannerReview extends EntityPathBase<PlannerReview> {

    private static final long serialVersionUID = 2066225078L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlannerReview plannerReview = new QPlannerReview("plannerReview");

    public final StringPath content = createString("content");

    public final DatePath<java.time.LocalDate> createdAt = createDate("createdAt", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final com.gudgo.jeju.domain.planner.entity.QPlanner planner;

    public final com.gudgo.jeju.domain.user.entity.QUser user;

    public QPlannerReview(String variable) {
        this(PlannerReview.class, forVariable(variable), INITS);
    }

    public QPlannerReview(Path<? extends PlannerReview> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlannerReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlannerReview(PathMetadata metadata, PathInits inits) {
        this(PlannerReview.class, metadata, inits);
    }

    public QPlannerReview(Class<? extends PlannerReview> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.planner = inits.isInitialized("planner") ? new com.gudgo.jeju.domain.planner.entity.QPlanner(forProperty("planner"), inits.get("planner")) : null;
        this.user = inits.isInitialized("user") ? new com.gudgo.jeju.domain.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}


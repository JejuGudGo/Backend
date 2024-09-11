package com.gudgo.jeju.domain.post.participant.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QParticipant is a Querydsl query type for Participant
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QParticipant extends EntityPathBase<Participant> {

    private static final long serialVersionUID = 1610133554L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QParticipant participant = new QParticipant("participant");

    public final DatePath<java.time.LocalDate> appliedAt = createDate("appliedAt", java.time.LocalDate.class);

    public final BooleanPath approved = createBoolean("approved");

    public final DatePath<java.time.LocalDate> approvedAt = createDate("approvedAt", java.time.LocalDate.class);

    public final StringPath content = createString("content");

    public final NumberPath<Long> count = createNumber("count", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isApplied = createBoolean("isApplied");

    public final BooleanPath isBlocked = createBoolean("isBlocked");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final com.gudgo.jeju.domain.planner.planner.entity.QPlanner planner;

    public final com.gudgo.jeju.domain.user.entity.QUser user;

    public QParticipant(String variable) {
        this(Participant.class, forVariable(variable), INITS);
    }

    public QParticipant(Path<? extends Participant> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QParticipant(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QParticipant(PathMetadata metadata, PathInits inits) {
        this(Participant.class, metadata, inits);
    }

    public QParticipant(Class<? extends Participant> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.planner = inits.isInitialized("planner") ? new com.gudgo.jeju.domain.planner.planner.entity.QPlanner(forProperty("planner"), inits.get("planner")) : null;
        this.user = inits.isInitialized("user") ? new com.gudgo.jeju.domain.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}


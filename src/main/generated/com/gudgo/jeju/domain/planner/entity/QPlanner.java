package com.gudgo.jeju.domain.planner.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlanner is a Querydsl query type for Planner
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlanner extends EntityPathBase<Planner> {

    private static final long serialVersionUID = 2142236578L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlanner planner = new QPlanner("planner");

    public final QChatRoom chatRoom;

    public final QCourse course;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isCompleted = createBoolean("isCompleted");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final BooleanPath isPrivate = createBoolean("isPrivate");

    public final DatePath<java.time.LocalDate> startAt = createDate("startAt", java.time.LocalDate.class);

    public final StringPath summary = createString("summary");

    public final TimePath<java.time.LocalTime> time = createTime("time", java.time.LocalTime.class);

    public final com.gudgo.jeju.domain.user.entity.QUser user;

    public QPlanner(String variable) {
        this(Planner.class, forVariable(variable), INITS);
    }

    public QPlanner(Path<? extends Planner> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlanner(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlanner(PathMetadata metadata, PathInits inits) {
        this(Planner.class, metadata, inits);
    }

    public QPlanner(Class<? extends Planner> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chatRoom = inits.isInitialized("chatRoom") ? new QChatRoom(forProperty("chatRoom")) : null;
        this.course = inits.isInitialized("course") ? new QCourse(forProperty("course")) : null;
        this.user = inits.isInitialized("user") ? new com.gudgo.jeju.domain.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}


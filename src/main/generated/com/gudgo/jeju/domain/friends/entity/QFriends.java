package com.gudgo.jeju.domain.friends.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFriends is a Querydsl query type for Friends
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFriends extends EntityPathBase<Friends> {

    private static final long serialVersionUID = 380703042L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFriends friends1 = new QFriends("friends1");

    public final QFriends friends;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isApproval = createBoolean("isApproval");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final BooleanPath isRequest = createBoolean("isRequest");

    public final com.gudgo.jeju.domain.user.entity.QUser user;

    public QFriends(String variable) {
        this(Friends.class, forVariable(variable), INITS);
    }

    public QFriends(Path<? extends Friends> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFriends(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFriends(PathMetadata metadata, PathInits inits) {
        this(Friends.class, metadata, inits);
    }

    public QFriends(Class<? extends Friends> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.friends = inits.isInitialized("friends") ? new QFriends(forProperty("friends"), inits.get("friends")) : null;
        this.user = inits.isInitialized("user") ? new com.gudgo.jeju.domain.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}


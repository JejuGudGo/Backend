package com.gudgo.jeju.domain.post.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPosts is a Querydsl query type for Posts
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPosts extends EntityPathBase<Posts> {

    private static final long serialVersionUID = 119807927L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPosts posts = new QPosts("posts");

    public final NumberPath<Long> companionsNum = createNumber("companionsNum", Long.class);

    public final StringPath content = createString("content");

    public final DatePath<java.time.LocalDate> createdAt = createDate("createdAt", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final BooleanPath isFinished = createBoolean("isFinished");

    public final NumberPath<Double> placeLatitude = createNumber("placeLatitude", Double.class);

    public final NumberPath<Double> placeLonggitude = createNumber("placeLonggitude", Double.class);

    public final StringPath placeName = createString("placeName");

    public final com.gudgo.jeju.domain.planner.entity.QPlanner planner;

    public final EnumPath<PostType> postType = createEnum("postType", PostType.class);

    public final StringPath title = createString("title");

    public final com.gudgo.jeju.domain.user.entity.QUser user;

    public QPosts(String variable) {
        this(Posts.class, forVariable(variable), INITS);
    }

    public QPosts(Path<? extends Posts> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPosts(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPosts(PathMetadata metadata, PathInits inits) {
        this(Posts.class, metadata, inits);
    }

    public QPosts(Class<? extends Posts> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.planner = inits.isInitialized("planner") ? new com.gudgo.jeju.domain.planner.entity.QPlanner(forProperty("planner"), inits.get("planner")) : null;
        this.user = inits.isInitialized("user") ? new com.gudgo.jeju.domain.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}


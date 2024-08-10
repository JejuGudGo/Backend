package com.gudgo.jeju.domain.profile.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProfile is a Querydsl query type for Profile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProfile extends EntityPathBase<Profile> {

    private static final long serialVersionUID = -47290686L;

    public static final QProfile profile = new QProfile("profile");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath profileImageUrl = createString("profileImageUrl");

    public final StringPath ranking = createString("ranking");

    public final TimePath<java.sql.Time> walkingTime = createTime("walkingTime", java.sql.Time.class);

    public QProfile(String variable) {
        super(Profile.class, forVariable(variable));
    }

    public QProfile(Path<? extends Profile> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProfile(PathMetadata metadata) {
        super(Profile.class, metadata);
    }

}


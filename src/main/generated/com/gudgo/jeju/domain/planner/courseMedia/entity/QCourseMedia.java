package com.gudgo.jeju.domain.planner.courseMedia.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCourseMedia is a Querydsl query type for CourseMedia
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCourseMedia extends EntityPathBase<CourseMedia> {

    private static final long serialVersionUID = -373780642L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCourseMedia courseMedia = new QCourseMedia("courseMedia");

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final com.gudgo.jeju.domain.planner.planner.entity.QPlanner planner;

    public QCourseMedia(String variable) {
        this(CourseMedia.class, forVariable(variable), INITS);
    }

    public QCourseMedia(Path<? extends CourseMedia> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCourseMedia(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCourseMedia(PathMetadata metadata, PathInits inits) {
        this(CourseMedia.class, metadata, inits);
    }

    public QCourseMedia(Class<? extends CourseMedia> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.planner = inits.isInitialized("planner") ? new com.gudgo.jeju.domain.planner.planner.entity.QPlanner(forProperty("planner"), inits.get("planner")) : null;
    }

}


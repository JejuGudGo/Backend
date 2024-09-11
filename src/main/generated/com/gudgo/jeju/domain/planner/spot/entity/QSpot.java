package com.gudgo.jeju.domain.planner.spot.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSpot is a Querydsl query type for Spot
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSpot extends EntityPathBase<Spot> {

    private static final long serialVersionUID = 665106332L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSpot spot = new QSpot("spot");

    public final StringPath address = createString("address");

    public final StringPath contentId = createString("contentId");

    public final NumberPath<Long> count = createNumber("count", Long.class);

    public final com.gudgo.jeju.domain.planner.course.entity.QCourse course;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isCompleted = createBoolean("isCompleted");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final NumberPath<Long> orderNumber = createNumber("orderNumber", Long.class);

    public final EnumPath<SpotType> spotType = createEnum("spotType", SpotType.class);

    public final StringPath title = createString("title");

    public final com.gudgo.jeju.domain.tourApi.entity.QTourApiCategory1 tourApiCategory1;

    public QSpot(String variable) {
        this(Spot.class, forVariable(variable), INITS);
    }

    public QSpot(Path<? extends Spot> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSpot(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSpot(PathMetadata metadata, PathInits inits) {
        this(Spot.class, metadata, inits);
    }

    public QSpot(Class<? extends Spot> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.course = inits.isInitialized("course") ? new com.gudgo.jeju.domain.planner.course.entity.QCourse(forProperty("course")) : null;
        this.tourApiCategory1 = inits.isInitialized("tourApiCategory1") ? new com.gudgo.jeju.domain.tourApi.entity.QTourApiCategory1(forProperty("tourApiCategory1"), inits.get("tourApiCategory1")) : null;
    }

}


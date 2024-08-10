package com.gudgo.jeju.domain.tourApi.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTourApiCategory2 is a Querydsl query type for TourApiCategory2
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTourApiCategory2 extends EntityPathBase<TourApiCategory2> {

    private static final long serialVersionUID = -941964430L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTourApiCategory2 tourApiCategory2 = new QTourApiCategory2("tourApiCategory2");

    public final StringPath categoryName = createString("categoryName");

    public final StringPath id = createString("id");

    public final QTourApiCategory1 tourApiCategory1;

    public QTourApiCategory2(String variable) {
        this(TourApiCategory2.class, forVariable(variable), INITS);
    }

    public QTourApiCategory2(Path<? extends TourApiCategory2> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTourApiCategory2(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTourApiCategory2(PathMetadata metadata, PathInits inits) {
        this(TourApiCategory2.class, metadata, inits);
    }

    public QTourApiCategory2(Class<? extends TourApiCategory2> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.tourApiCategory1 = inits.isInitialized("tourApiCategory1") ? new QTourApiCategory1(forProperty("tourApiCategory1"), inits.get("tourApiCategory1")) : null;
    }

}


package com.gudgo.jeju.domain.tourApi.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTourApiCategory3 is a Querydsl query type for TourApiCategory3
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTourApiCategory3 extends EntityPathBase<TourApiCategory3> {

    private static final long serialVersionUID = -941964429L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTourApiCategory3 tourApiCategory3 = new QTourApiCategory3("tourApiCategory3");

    public final StringPath categoryName = createString("categoryName");

    public final StringPath id = createString("id");

    public final QTourApiCategory2 tourApiCategory2;

    public QTourApiCategory3(String variable) {
        this(TourApiCategory3.class, forVariable(variable), INITS);
    }

    public QTourApiCategory3(Path<? extends TourApiCategory3> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTourApiCategory3(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTourApiCategory3(PathMetadata metadata, PathInits inits) {
        this(TourApiCategory3.class, metadata, inits);
    }

    public QTourApiCategory3(Class<? extends TourApiCategory3> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.tourApiCategory2 = inits.isInitialized("tourApiCategory2") ? new QTourApiCategory2(forProperty("tourApiCategory2"), inits.get("tourApiCategory2")) : null;
    }

}


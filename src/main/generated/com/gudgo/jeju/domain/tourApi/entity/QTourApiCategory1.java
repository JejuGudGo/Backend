package com.gudgo.jeju.domain.tourApi.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTourApiCategory1 is a Querydsl query type for TourApiCategory1
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTourApiCategory1 extends EntityPathBase<TourApiCategory1> {

    private static final long serialVersionUID = -941964431L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTourApiCategory1 tourApiCategory1 = new QTourApiCategory1("tourApiCategory1");

    public final StringPath categoryName = createString("categoryName");

    public final StringPath id = createString("id");

    public final QTourApiContentType tourApiContentType;

    public QTourApiCategory1(String variable) {
        this(TourApiCategory1.class, forVariable(variable), INITS);
    }

    public QTourApiCategory1(Path<? extends TourApiCategory1> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTourApiCategory1(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTourApiCategory1(PathMetadata metadata, PathInits inits) {
        this(TourApiCategory1.class, metadata, inits);
    }

    public QTourApiCategory1(Class<? extends TourApiCategory1> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.tourApiContentType = inits.isInitialized("tourApiContentType") ? new QTourApiContentType(forProperty("tourApiContentType")) : null;
    }

}


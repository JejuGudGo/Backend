package com.gudgo.jeju.domain.tourApi.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTourApiContent is a Querydsl query type for TourApiContent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTourApiContent extends EntityPathBase<TourApiContent> {

    private static final long serialVersionUID = -610853769L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTourApiContent tourApiContent = new QTourApiContent("tourApiContent");

    public final StringPath contentTypeId = createString("contentTypeId");

    public final NumberPath<Long> count = createNumber("count", Long.class);

    public final StringPath id = createString("id");

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final QTourApiCategory3 tourApiCategory3;

    public final QTourApiContentInfo tourApiContentInfo;

    public final StringPath updatedAt = createString("updatedAt");

    public QTourApiContent(String variable) {
        this(TourApiContent.class, forVariable(variable), INITS);
    }

    public QTourApiContent(Path<? extends TourApiContent> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTourApiContent(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTourApiContent(PathMetadata metadata, PathInits inits) {
        this(TourApiContent.class, metadata, inits);
    }

    public QTourApiContent(Class<? extends TourApiContent> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.tourApiCategory3 = inits.isInitialized("tourApiCategory3") ? new QTourApiCategory3(forProperty("tourApiCategory3"), inits.get("tourApiCategory3")) : null;
        this.tourApiContentInfo = inits.isInitialized("tourApiContentInfo") ? new QTourApiContentInfo(forProperty("tourApiContentInfo")) : null;
    }

}


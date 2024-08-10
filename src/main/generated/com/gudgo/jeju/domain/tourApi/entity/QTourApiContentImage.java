package com.gudgo.jeju.domain.tourApi.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTourApiContentImage is a Querydsl query type for TourApiContentImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTourApiContentImage extends EntityPathBase<TourApiContentImage> {

    private static final long serialVersionUID = 1640156964L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTourApiContentImage tourApiContentImage = new QTourApiContentImage("tourApiContentImage");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final QTourApiContentInfo tourApiContentInfo;

    public QTourApiContentImage(String variable) {
        this(TourApiContentImage.class, forVariable(variable), INITS);
    }

    public QTourApiContentImage(Path<? extends TourApiContentImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTourApiContentImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTourApiContentImage(PathMetadata metadata, PathInits inits) {
        this(TourApiContentImage.class, metadata, inits);
    }

    public QTourApiContentImage(Class<? extends TourApiContentImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.tourApiContentInfo = inits.isInitialized("tourApiContentInfo") ? new QTourApiContentInfo(forProperty("tourApiContentInfo")) : null;
    }

}


package com.gudgo.jeju.domain.tourApi.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTourApiContentType is a Querydsl query type for TourApiContentType
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTourApiContentType extends EntityPathBase<TourApiContentType> {

    private static final long serialVersionUID = -916583343L;

    public static final QTourApiContentType tourApiContentType = new QTourApiContentType("tourApiContentType");

    public final StringPath id = createString("id");

    public final StringPath title = createString("title");

    public QTourApiContentType(String variable) {
        super(TourApiContentType.class, forVariable(variable));
    }

    public QTourApiContentType(Path<? extends TourApiContentType> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTourApiContentType(PathMetadata metadata) {
        super(TourApiContentType.class, metadata);
    }

}


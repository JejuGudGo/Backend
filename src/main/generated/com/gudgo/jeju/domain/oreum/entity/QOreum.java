package com.gudgo.jeju.domain.oreum.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOreum is a Querydsl query type for Oreum
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOreum extends EntityPathBase<Oreum> {

    private static final long serialVersionUID = -245662L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOreum oreum = new QOreum("oreum");

    public final StringPath address = createString("address");

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final StringPath title = createString("title");

    public final com.gudgo.jeju.domain.tourApi.entity.QTourApiCategory1 tourApiCategory1;

    public final DatePath<java.time.LocalDate> updatedAt = createDate("updatedAt", java.time.LocalDate.class);

    public QOreum(String variable) {
        this(Oreum.class, forVariable(variable), INITS);
    }

    public QOreum(Path<? extends Oreum> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOreum(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOreum(PathMetadata metadata, PathInits inits) {
        this(Oreum.class, metadata, inits);
    }

    public QOreum(Class<? extends Oreum> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.tourApiCategory1 = inits.isInitialized("tourApiCategory1") ? new com.gudgo.jeju.domain.tourApi.entity.QTourApiCategory1(forProperty("tourApiCategory1"), inits.get("tourApiCategory1")) : null;
    }

}


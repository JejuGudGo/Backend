package com.gudgo.jeju.domain.olle.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QJeJuOlleSpot is a Querydsl query type for JeJuOlleSpot
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QJeJuOlleSpot extends EntityPathBase<JeJuOlleSpot> {

    private static final long serialVersionUID = -1838254000L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QJeJuOlleSpot jeJuOlleSpot = new QJeJuOlleSpot("jeJuOlleSpot");

    public final StringPath distance = createString("distance");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QJeJuOlleCourse jeJuOlleCourse;

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final NumberPath<Long> orderNumber = createNumber("orderNumber", Long.class);

    public final StringPath title = createString("title");

    public QJeJuOlleSpot(String variable) {
        this(JeJuOlleSpot.class, forVariable(variable), INITS);
    }

    public QJeJuOlleSpot(Path<? extends JeJuOlleSpot> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QJeJuOlleSpot(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QJeJuOlleSpot(PathMetadata metadata, PathInits inits) {
        this(JeJuOlleSpot.class, metadata, inits);
    }

    public QJeJuOlleSpot(Class<? extends JeJuOlleSpot> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.jeJuOlleCourse = inits.isInitialized("jeJuOlleCourse") ? new QJeJuOlleCourse(forProperty("jeJuOlleCourse")) : null;
    }

}


package com.gudgo.jeju.domain.olle.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QJeJuOlleCourseData is a Querydsl query type for JeJuOlleCourseData
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QJeJuOlleCourseData extends EntityPathBase<JeJuOlleCourseData> {

    private static final long serialVersionUID = -231535821L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QJeJuOlleCourseData jeJuOlleCourseData = new QJeJuOlleCourseData("jeJuOlleCourseData");

    public final NumberPath<Double> altitude = createNumber("altitude", Double.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QJeJuOlleCourse jeJuOlleCourse;

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final NumberPath<Long> orderNumber = createNumber("orderNumber", Long.class);

    public final DateTimePath<java.time.OffsetDateTime> time = createDateTime("time", java.time.OffsetDateTime.class);

    public final DatePath<java.time.LocalDate> updatedAt = createDate("updatedAt", java.time.LocalDate.class);

    public QJeJuOlleCourseData(String variable) {
        this(JeJuOlleCourseData.class, forVariable(variable), INITS);
    }

    public QJeJuOlleCourseData(Path<? extends JeJuOlleCourseData> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QJeJuOlleCourseData(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QJeJuOlleCourseData(PathMetadata metadata, PathInits inits) {
        this(JeJuOlleCourseData.class, metadata, inits);
    }

    public QJeJuOlleCourseData(Class<? extends JeJuOlleCourseData> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.jeJuOlleCourse = inits.isInitialized("jeJuOlleCourse") ? new QJeJuOlleCourse(forProperty("jeJuOlleCourse")) : null;
    }

}


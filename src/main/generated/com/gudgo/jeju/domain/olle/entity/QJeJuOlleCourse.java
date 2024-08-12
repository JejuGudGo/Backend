package com.gudgo.jeju.domain.olle.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QJeJuOlleCourse is a Querydsl query type for JeJuOlleCourse
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QJeJuOlleCourse extends EntityPathBase<JeJuOlleCourse> {

    private static final long serialVersionUID = -1789344791L;

    public static final QJeJuOlleCourse jeJuOlleCourse = new QJeJuOlleCourse("jeJuOlleCourse");

    public final StringPath courseNumber = createString("courseNumber");

    public final NumberPath<Double> endLatitude = createNumber("endLatitude", Double.class);

    public final NumberPath<Double> endLongitude = createNumber("endLongitude", Double.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<OlleType> olleType = createEnum("olleType", OlleType.class);

    public final NumberPath<Double> startLatitude = createNumber("startLatitude", Double.class);

    public final NumberPath<Double> startLongitude = createNumber("startLongitude", Double.class);

    public final StringPath title = createString("title");

    public final StringPath totalDistance = createString("totalDistance");

    public final StringPath totalTime = createString("totalTime");

    public final BooleanPath wheelchairAccessible = createBoolean("wheelchairAccessible");

    public QJeJuOlleCourse(String variable) {
        super(JeJuOlleCourse.class, forVariable(variable));
    }

    public QJeJuOlleCourse(Path<? extends JeJuOlleCourse> path) {
        super(path.getType(), path.getMetadata());
    }

    public QJeJuOlleCourse(PathMetadata metadata) {
        super(JeJuOlleCourse.class, metadata);
    }

}


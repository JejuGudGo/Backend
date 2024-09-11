package com.gudgo.jeju.domain.planner.course.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCourseInfo is a Querydsl query type for CourseInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCourseInfo extends EntityPathBase<CourseInfo> {

    private static final long serialVersionUID = 1345074844L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCourseInfo courseInfo = new QCourseInfo("courseInfo");

    public final StringPath address = createString("address");

    public final StringPath content = createString("content");

    public final QCourse course;

    public final EnumPath<CourseType> courseType = createEnum("courseType", CourseType.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final NumberPath<Long> orderNumber = createNumber("orderNumber", Long.class);

    public final StringPath title = createString("title");

    public QCourseInfo(String variable) {
        this(CourseInfo.class, forVariable(variable), INITS);
    }

    public QCourseInfo(Path<? extends CourseInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCourseInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCourseInfo(PathMetadata metadata, PathInits inits) {
        this(CourseInfo.class, metadata, inits);
    }

    public QCourseInfo(Class<? extends CourseInfo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.course = inits.isInitialized("course") ? new QCourse(forProperty("course")) : null;
    }

}


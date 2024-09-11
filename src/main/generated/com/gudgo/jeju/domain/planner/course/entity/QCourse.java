package com.gudgo.jeju.domain.planner.course.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCourse is a Querydsl query type for Course
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCourse extends EntityPathBase<Course> {

    private static final long serialVersionUID = -938707762L;

    public static final QCourse course = new QCourse("course");

    public final DatePath<java.time.LocalDate> createdAt = createDate("createdAt", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> olleCourseId = createNumber("olleCourseId", Long.class);

    public final NumberPath<Long> originalCourseId = createNumber("originalCourseId", Long.class);

    public final NumberPath<Long> originalCreatorId = createNumber("originalCreatorId", Long.class);

    public final NumberPath<Double> starAvg = createNumber("starAvg", Double.class);

    public final StringPath title = createString("title");

    public final EnumPath<CourseType> type = createEnum("type", CourseType.class);

    public QCourse(String variable) {
        super(Course.class, forVariable(variable));
    }

    public QCourse(Path<? extends Course> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCourse(PathMetadata metadata) {
        super(Course.class, metadata);
    }

}


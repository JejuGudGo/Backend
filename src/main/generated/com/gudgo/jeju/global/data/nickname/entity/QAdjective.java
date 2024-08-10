package com.gudgo.jeju.global.data.nickname.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAdjective is a Querydsl query type for Adjective
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdjective extends EntityPathBase<Adjective> {

    private static final long serialVersionUID = 1156948058L;

    public static final QAdjective adjective1 = new QAdjective("adjective1");

    public final StringPath adjective = createString("adjective");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QAdjective(String variable) {
        super(Adjective.class, forVariable(variable));
    }

    public QAdjective(Path<? extends Adjective> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAdjective(PathMetadata metadata) {
        super(Adjective.class, metadata);
    }

}


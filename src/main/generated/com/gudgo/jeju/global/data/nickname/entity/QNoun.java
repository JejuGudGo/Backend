package com.gudgo.jeju.global.data.nickname.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QNoun is a Querydsl query type for Noun
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNoun extends EntityPathBase<Noun> {

    private static final long serialVersionUID = 333278729L;

    public static final QNoun noun1 = new QNoun("noun1");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath noun = createString("noun");

    public QNoun(String variable) {
        super(Noun.class, forVariable(variable));
    }

    public QNoun(Path<? extends Noun> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNoun(PathMetadata metadata) {
        super(Noun.class, metadata);
    }

}


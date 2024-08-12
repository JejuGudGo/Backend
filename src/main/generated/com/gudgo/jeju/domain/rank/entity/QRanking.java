package com.gudgo.jeju.domain.rank.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRanking is a Querydsl query type for Ranking
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRanking extends EntityPathBase<Ranking> {

    private static final long serialVersionUID = 2049001998L;

    public static final QRanking ranking = new QRanking("ranking");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath level = createString("level");

    public final TimePath<java.sql.Time> maximum = createTime("maximum", java.sql.Time.class);

    public final TimePath<java.sql.Time> minimum = createTime("minimum", java.sql.Time.class);

    public final StringPath title = createString("title");

    public QRanking(String variable) {
        super(Ranking.class, forVariable(variable));
    }

    public QRanking(Path<? extends Ranking> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRanking(PathMetadata metadata) {
        super(Ranking.class, metadata);
    }

}


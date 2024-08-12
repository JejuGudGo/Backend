package com.gudgo.jeju.global.data.common.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDataConfiguration is a Querydsl query type for DataConfiguration
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDataConfiguration extends EntityPathBase<DataConfiguration> {

    private static final long serialVersionUID = -1501121536L;

    public static final QDataConfiguration dataConfiguration = new QDataConfiguration("dataConfiguration");

    public final StringPath configKey = createString("configKey");

    public final BooleanPath configValue = createBoolean("configValue");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DatePath<java.time.LocalDate> updatedAt = createDate("updatedAt", java.time.LocalDate.class);

    public QDataConfiguration(String variable) {
        super(DataConfiguration.class, forVariable(variable));
    }

    public QDataConfiguration(Path<? extends DataConfiguration> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDataConfiguration(PathMetadata metadata) {
        super(DataConfiguration.class, metadata);
    }

}


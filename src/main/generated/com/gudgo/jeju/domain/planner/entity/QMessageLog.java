package com.gudgo.jeju.domain.planner.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMessageLog is a Querydsl query type for MessageLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMessageLog extends EntityPathBase<MessageLog> {

    private static final long serialVersionUID = 1885387245L;

    public static final QMessageLog messageLog = new QMessageLog("messageLog");

    public final NumberPath<Long> chatRoomId = createNumber("chatRoomId", Long.class);

    public final StringPath errorMessage = createString("errorMessage");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<LogStatus> status = createEnum("status", LogStatus.class);

    public final DateTimePath<java.time.LocalDateTime> timestamp = createDateTime("timestamp", java.time.LocalDateTime.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QMessageLog(String variable) {
        super(MessageLog.class, forVariable(variable));
    }

    public QMessageLog(Path<? extends MessageLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMessageLog(PathMetadata metadata) {
        super(MessageLog.class, metadata);
    }

}


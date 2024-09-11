package com.gudgo.jeju.domain.post.chat.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMessageImage is a Querydsl query type for MessageImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMessageImage extends EntityPathBase<MessageImage> {

    private static final long serialVersionUID = -1455808104L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMessageImage messageImage = new QMessageImage("messageImage");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMessage message;

    public final StringPath messageImageUrl = createString("messageImageUrl");

    public QMessageImage(String variable) {
        this(MessageImage.class, forVariable(variable), INITS);
    }

    public QMessageImage(Path<? extends MessageImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMessageImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMessageImage(PathMetadata metadata, PathInits inits) {
        this(MessageImage.class, metadata, inits);
    }

    public QMessageImage(Class<? extends MessageImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.message = inits.isInitialized("message") ? new QMessage(forProperty("message"), inits.get("message")) : null;
    }

}


package com.gudgo.jeju.domain.tourApi.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTourApiContentInfo is a Querydsl query type for TourApiContentInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTourApiContentInfo extends EntityPathBase<TourApiContentInfo> {

    private static final long serialVersionUID = -916921915L;

    public static final QTourApiContentInfo tourApiContentInfo = new QTourApiContentInfo("tourApiContentInfo");

    public final StringPath address = createString("address");

    public final StringPath availablePet = createString("availablePet");

    public final StringPath closeDay = createString("closeDay");

    public final StringPath content = createString("content");

    public final StringPath eventContent = createString("eventContent");

    public final StringPath eventEndDate = createString("eventEndDate");

    public final StringPath eventFee = createString("eventFee");

    public final StringPath eventPlace = createString("eventPlace");

    public final StringPath eventStartDate = createString("eventStartDate");

    public final StringPath fee = createString("fee");

    public final StringPath guideService = createString("guideService");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath info = createString("info");

    public final StringPath organizeNumber = createString("organizeNumber");

    public final StringPath organizerInfo = createString("organizerInfo");

    public final StringPath pageUrl = createString("pageUrl");

    public final StringPath park = createString("park");

    public final StringPath rentStroller = createString("rentStroller");

    public final StringPath reserveInfo = createString("reserveInfo");

    public final StringPath time = createString("time");

    public final StringPath title = createString("title");

    public final StringPath toilet = createString("toilet");

    public QTourApiContentInfo(String variable) {
        super(TourApiContentInfo.class, forVariable(variable));
    }

    public QTourApiContentInfo(Path<? extends TourApiContentInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTourApiContentInfo(PathMetadata metadata) {
        super(TourApiContentInfo.class, metadata);
    }

}


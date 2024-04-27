package com.example.airlineproject.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOffice is a Querydsl query type for Office
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOffice extends EntityPathBase<Office> {

    private static final long serialVersionUID = 1611331077L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOffice office = new QOffice("office");

    public final StringPath city = createString("city");

    public final QCompany company;

    public final StringPath country = createString("country");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath phone = createString("phone");

    public final StringPath street = createString("street");

    public final DateTimePath<java.util.Date> workEndTime = createDateTime("workEndTime", java.util.Date.class);

    public final DateTimePath<java.util.Date> workStartTime = createDateTime("workStartTime", java.util.Date.class);

    public QOffice(String variable) {
        this(Office.class, forVariable(variable), INITS);
    }

    public QOffice(Path<? extends Office> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOffice(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOffice(PathMetadata metadata, PathInits inits) {
        this(Office.class, metadata, inits);
    }

    public QOffice(Class<? extends Office> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new QCompany(forProperty("company"), inits.get("company")) : null;
    }

}


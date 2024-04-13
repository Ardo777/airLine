package com.example.airlineproject.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFlight is a Querydsl query type for Flight
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFlight extends EntityPathBase<Flight> {

    private static final long serialVersionUID = 1359297465L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFlight flight = new QFlight("flight");

    public final DateTimePath<java.time.LocalDateTime> arrivalTime = createDateTime("arrivalTime", java.time.LocalDateTime.class);

    public final QCompany company;

    public final DateTimePath<java.time.LocalDateTime> estimatedTime = createDateTime("estimatedTime", java.time.LocalDateTime.class);

    public final StringPath from = createString("from");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final QPlane plane;

    public final DateTimePath<java.time.LocalDateTime> scheduledTime = createDateTime("scheduledTime", java.time.LocalDateTime.class);

    public final EnumPath<com.example.airlineproject.entity.enums.Status> status = createEnum("status", com.example.airlineproject.entity.enums.Status.class);

    public final StringPath to = createString("to");

    public QFlight(String variable) {
        this(Flight.class, forVariable(variable), INITS);
    }

    public QFlight(Path<? extends Flight> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFlight(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFlight(PathMetadata metadata, PathInits inits) {
        this(Flight.class, metadata, inits);
    }

    public QFlight(Class<? extends Flight> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new QCompany(forProperty("company"), inits.get("company")) : null;
        this.plane = inits.isInitialized("plane") ? new QPlane(forProperty("plane"), inits.get("plane")) : null;
    }

}


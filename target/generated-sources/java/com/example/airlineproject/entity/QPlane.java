package com.example.airlineproject.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlane is a Querydsl query type for Plane
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlane extends EntityPathBase<Plane> {

    private static final long serialVersionUID = 1577096691L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlane plane = new QPlane("plane");

    public final QCompany company;

    public final NumberPath<Integer> countBusiness = createNumber("countBusiness", Integer.class);

    public final NumberPath<Integer> countEconomy = createNumber("countEconomy", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Double> maxBaggage = createNumber("maxBaggage", Double.class);

    public final StringPath model = createString("model");

    public final StringPath planePic = createString("planePic");

    public QPlane(String variable) {
        this(Plane.class, forVariable(variable), INITS);
    }

    public QPlane(Path<? extends Plane> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlane(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlane(PathMetadata metadata, PathInits inits) {
        this(Plane.class, metadata, inits);
    }

    public QPlane(Class<? extends Plane> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new QCompany(forProperty("company"), inits.get("company")) : null;
    }

}


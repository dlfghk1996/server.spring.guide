package server.spring.guide.event.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserPay is a Querydsl query type for UserPay
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserPay extends EntityPathBase<UserPay> {

    private static final long serialVersionUID = -82976849L;

    public static final QUserPay userPay = new QUserPay("userPay");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    public final NumberPath<Integer> stamp = createNumber("stamp", Integer.class);

    public final NumberPath<Integer> totalStamp = createNumber("totalStamp", Integer.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUserPay(String variable) {
        super(UserPay.class, forVariable(variable));
    }

    public QUserPay(Path<? extends UserPay> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserPay(PathMetadata metadata) {
        super(UserPay.class, metadata);
    }

}


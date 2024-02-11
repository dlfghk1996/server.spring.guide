package server.spring.guide.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserLikeUp is a Querydsl query type for UserLikeUp
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserLikeUp extends EntityPathBase<UserLikeUp> {

    private static final long serialVersionUID = -1317028148L;

    public static final QUserLikeUp userLikeUp = new QUserLikeUp("userLikeUp");

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final NumberPath<Long> version = createNumber("version", Long.class);

    public QUserLikeUp(String variable) {
        super(UserLikeUp.class, forVariable(variable));
    }

    public QUserLikeUp(Path<? extends UserLikeUp> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserLikeUp(PathMetadata metadata) {
        super(UserLikeUp.class, metadata);
    }

}


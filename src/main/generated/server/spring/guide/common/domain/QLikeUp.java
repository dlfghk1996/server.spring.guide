package server.spring.guide.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLikeUp is a Querydsl query type for LikeUp
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLikeUp extends EntityPathBase<LikeUp> {

    private static final long serialVersionUID = -1275540831L;

    public static final QLikeUp likeUp = new QLikeUp("likeUp");

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QLikeUp(String variable) {
        super(LikeUp.class, forVariable(variable));
    }

    public QLikeUp(Path<? extends LikeUp> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLikeUp(PathMetadata metadata) {
        super(LikeUp.class, metadata);
    }

}


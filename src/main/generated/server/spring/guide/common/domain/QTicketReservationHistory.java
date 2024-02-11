package server.spring.guide.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTicketReservationHistory is a Querydsl query type for TicketReservationHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTicketReservationHistory extends EntityPathBase<TicketReservationHistory> {

    private static final long serialVersionUID = 1232235427L;

    public static final QTicketReservationHistory ticketReservationHistory = new QTicketReservationHistory("ticketReservationHistory");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> ticketId = createNumber("ticketId", Long.class);

    public final NumberPath<Integer> ticketNumber = createNumber("ticketNumber", Integer.class);

    public QTicketReservationHistory(String variable) {
        super(TicketReservationHistory.class, forVariable(variable));
    }

    public QTicketReservationHistory(Path<? extends TicketReservationHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTicketReservationHistory(PathMetadata metadata) {
        super(TicketReservationHistory.class, metadata);
    }

}


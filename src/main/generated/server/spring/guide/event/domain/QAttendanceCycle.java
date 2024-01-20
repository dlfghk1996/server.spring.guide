package server.spring.guide.event.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAttendanceCycle is a Querydsl query type for AttendanceCycle
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAttendanceCycle extends EntityPathBase<AttendanceCycle> {

    private static final long serialVersionUID = 959670351L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAttendanceCycle attendanceCycle = new QAttendanceCycle("attendanceCycle");

    public final ListPath<AttendanceCheck, QAttendanceCheck> attendanceChecks = this.<AttendanceCheck, QAttendanceCheck>createList("attendanceChecks", AttendanceCheck.class, QAttendanceCheck.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<java.time.Month> month = createEnum("month", java.time.Month.class);

    public final EnumPath<server.spring.guide.event.domain.enums.Progress> progress = createEnum("progress", server.spring.guide.event.domain.enums.Progress.class);

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public final NumberPath<Integer> step = createNumber("step", Integer.class);

    public final server.spring.guide.common.domain.QUser user;

    public QAttendanceCycle(String variable) {
        this(AttendanceCycle.class, forVariable(variable), INITS);
    }

    public QAttendanceCycle(Path<? extends AttendanceCycle> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAttendanceCycle(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAttendanceCycle(PathMetadata metadata, PathInits inits) {
        this(AttendanceCycle.class, metadata, inits);
    }

    public QAttendanceCycle(Class<? extends AttendanceCycle> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new server.spring.guide.common.domain.QUser(forProperty("user")) : null;
    }

}


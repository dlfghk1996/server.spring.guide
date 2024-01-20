package server.spring.guide.event.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAttendanceCheck is a Querydsl query type for AttendanceCheck
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAttendanceCheck extends EntityPathBase<AttendanceCheck> {

    private static final long serialVersionUID = 959165553L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAttendanceCheck attendanceCheck = new QAttendanceCheck("attendanceCheck");

    public final QAttendanceCycle attendanceCycle;

    public final DatePath<java.time.LocalDate> attendanceDate = createDate("attendanceDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<server.spring.guide.event.domain.enums.Progress> progress = createEnum("progress", server.spring.guide.event.domain.enums.Progress.class);

    public QAttendanceCheck(String variable) {
        this(AttendanceCheck.class, forVariable(variable), INITS);
    }

    public QAttendanceCheck(Path<? extends AttendanceCheck> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAttendanceCheck(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAttendanceCheck(PathMetadata metadata, PathInits inits) {
        this(AttendanceCheck.class, metadata, inits);
    }

    public QAttendanceCheck(Class<? extends AttendanceCheck> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.attendanceCycle = inits.isInitialized("attendanceCycle") ? new QAttendanceCycle(forProperty("attendanceCycle"), inits.get("attendanceCycle")) : null;
    }

}


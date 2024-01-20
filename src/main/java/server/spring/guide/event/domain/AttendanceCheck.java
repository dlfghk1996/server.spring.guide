package server.spring.guide.event.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import server.spring.guide.event.domain.enums.Progress;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AttendanceCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "attendance_cycle_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AttendanceCycle attendanceCycle;

    private LocalDate attendanceDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Progress progress;

    public AttendanceCheck(AttendanceCycle attendanceCycle, LocalDate attendanceTime, Progress progress) {
        this.attendanceCycle = attendanceCycle;
        this.attendanceDate = attendanceTime;
        this.progress = progress;
    }

    public void setAttendanceCycle(AttendanceCycle attendanceCycle) {
        this.attendanceCycle=attendanceCycle;
    }
}

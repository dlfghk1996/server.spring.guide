package server.spring.guide.event.domain;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import server.spring.guide.common.domain.User;
import server.spring.guide.event.domain.enums.Progress;
import server.spring.guide.event.domain.enums.Step;


/*
 * 사이클(Cycle):
 * 총 3번의 인증으로 이루어져 있으며 3번의 인증을 마치면
 * 챌린지에 대해 한 번의 사이클을 성공(NOTHING -> FIRST -> SECOND -> SUCCESS)
 */

@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AttendanceCycle {

    public static final long DAYS = 3L;
    private static final int MAX_CYCLE_DETAILS_SIZE = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Enumerated(EnumType.STRING)
    private Month month;

    @OneToMany(mappedBy = "attendanceCycle", cascade = CascadeType.ALL)
    @BatchSize(size = 10)
    private List<AttendanceCheck> attendanceChecks = new ArrayList<>();

    @Column(nullable = false)
    private int step;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Progress progress;

    @Column(nullable = false)
    private LocalDate startDate;

    public AttendanceCycle(User user, Month month, int step, Progress progress, LocalDate startDate) {
        this.user = user;
        this.month = month;
        this.step = step;
        this.progress = progress;
        this.startDate = startDate;
    }


    public void increaseProgress(LocalDate attendanceTime) {
        System.out.println("IncreaseProgress");
        System.out.println(startDate);
        System.out.println(attendanceTime);
        this.progress = progress.increase(startDate, attendanceTime);
    }

    public void addAttendanceCheck(AttendanceCheck attendanceCheck){
        this.attendanceChecks.add(attendanceCheck);
        attendanceCheck.setAttendanceCycle(this);
    }
}

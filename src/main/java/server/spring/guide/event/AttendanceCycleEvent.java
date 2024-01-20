package server.spring.guide.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.spring.guide.event.domain.AttendanceCycle;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AttendanceCycleEvent {
    private AttendanceCycle attendanceCycle;
}

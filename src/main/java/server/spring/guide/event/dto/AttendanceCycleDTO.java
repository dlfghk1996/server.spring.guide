package server.spring.guide.event.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class AttendanceCycleDTO {
    private Long userId;
    private int step;
    private LocalDate attendanceDate;
}

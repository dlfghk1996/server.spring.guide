package server.spring.guide.event.domain.enums;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.bytebuddy.implementation.bytecode.Throw;

@RequiredArgsConstructor
@Getter
public enum Progress {
    NOTHING(0) {
        @Override
        public Progress increase(LocalDate startTime, LocalDate attendanceTime) {
            if (startTime.compareTo(attendanceTime) == 0) {
                return FIRST;
            }
            return null;
        }
    },
    FIRST(1) {
        @Override
        public Progress increase(LocalDate startDate, LocalDate attendanceDate) {
            if (ChronoUnit.DAYS.between(startDate, attendanceDate) == 1) {
                return SECOND;
            }
            return null;
        }
    },
    SECOND(2) {
        @Override
        public Progress increase(LocalDate startDate, LocalDate attendanceDate) {
            if (ChronoUnit.DAYS.between(startDate, attendanceDate) == 2) {
                return SUCCESS;
            }
            return null;
        }
    },
    SUCCESS(3) {
        @Override
        public Progress increase(LocalDate startDate, LocalDate progressTime) {
           return null; // (ExceptionData.ALREADY_SUCCESS);
        }

    };

    private final int count;

    public abstract Progress increase(LocalDate startDate, LocalDate progressTime);

    private static boolean isBetween(LocalDate startDate, LocalDate progressTime, Long interval) {
        LocalDate fromTime = startDate.plusDays(interval - 1L);
        LocalDate toTime = startDate.plusDays(interval);
        return (progressTime.isEqual(fromTime) || progressTime.isAfter(fromTime))
            && progressTime.isBefore(toTime);
    }
}

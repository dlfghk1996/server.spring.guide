package server.spring.guide.event.service;


import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.Month;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import server.spring.guide.common.repository.UserRepository;
import server.spring.guide.event.domain.AttendanceCheck;
import server.spring.guide.event.domain.AttendanceCycle;
import server.spring.guide.event.domain.enums.Progress;
import server.spring.guide.event.domain.enums.Step;
import server.spring.guide.event.dto.AttendanceCycleDTO;
import server.spring.guide.event.repository.AttendanceCycleRepository;



@RequiredArgsConstructor
@Service
@Slf4j
public class AttendanceCycleService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserRepository userRepository;
    private final AttendanceCycleRepository attendanceCycleRepository;
    private final ModelMapper mapper;

    @Transactional(rollbackOn = Exception.class)
    public AttendanceCycleDTO add(AttendanceCycleDTO request) throws Exception {
        Month month = LocalDate.now().getMonth();
        // 해달 월 사이클 가져오기
        //    AttendanceCycle attendanceCycle = attendanceCycleRepository.findByIdWithLock(userId, month)
        AttendanceCycle attendanceCycle =
            attendanceCycleRepository.findByUserIdAndMonthAndStep(request.getUserId(), month, request.getStep())
                .orElseGet(()->new AttendanceCycle(
                    userRepository.findById(request.getUserId()).get(),
                    month,
                    Step.FIRST.getValue(),
                    Progress.NOTHING,
                    LocalDate.now()));

        attendanceCycle.increaseProgress(request.getAttendanceDate());

        if (attendanceCycle.getAttendanceChecks().size() < 3) {
            attendanceCycle.addAttendanceCheck(new AttendanceCheck(
                attendanceCycle,
                request.getAttendanceDate(),
                attendanceCycle.getProgress()));
        }

        // add CycleDetail
        attendanceCycle = attendanceCycleRepository.save(attendanceCycle);

        // 이벤트 발행
        applicationEventPublisher.publishEvent(attendanceCycle);

        return mapper.map(attendanceCycle,AttendanceCycleDTO.class);
    }
}

package server.spring.guide.event.listener;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import server.spring.guide.event.AttendanceCycleEvent;
import server.spring.guide.event.domain.AttendanceCycle;
import server.spring.guide.event.domain.UserPay;
import server.spring.guide.event.domain.enums.Progress;
import server.spring.guide.event.service.UserPayService;
@Slf4j
@Component
@RequiredArgsConstructor
public class UserPayListener {

    private final UserPayService userPayService;

    // 출석 체크 이벤트를 구독한다.
    @TransactionalEventListener
    @Async
    public void on(AttendanceCycle attendanceCycle) throws Exception {

        log.info("Listener Thread : " + String.valueOf(Thread.currentThread().getId()));
        UserPay userPay = userPayService.add(attendanceCycle.getUser().getId(), Progress.FIRST);

    }
}

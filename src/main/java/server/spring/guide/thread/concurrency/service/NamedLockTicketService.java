package server.spring.guide.thread.concurrency.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.spring.guide.common.domain.Ticket;
import server.spring.guide.common.domain.TicketReservationHistory;
import server.spring.guide.common.domain.UserLikeUp;
import server.spring.guide.common.repository.TicketReservationHistoryRepository;
import server.spring.guide.common.repository.TicketingRepository;
import server.spring.guide.common.repository.UserLikeUpRepository;
import server.spring.guide.common.service.UserLikeUpService;

@Slf4j
@RequiredArgsConstructor
@Service
public class NamedLockTicketService {

    private final TicketingRepository ticketRepository;

    private final TicketReservationHistoryRepository ticketReservationHistoryRepository;


    /* Named Lock (MySQL Native Named Lock-분산락 구현)
     * MetaData 단위에 대한 Lock
    * 이름을 가진 Lock을 획득하여 해제할 때까지 다른 세션은 해당 Lock을 획득할 수 없다.
    * 테이블 단위로 Lock 휙득 - MySQL 서버 메모리 직접 동작
     */

    /* 주의점 (Lock 해제와 세션(데이터 소스 분리 시) 수동 관리)
     * 1. Transaction이 종료될 때 Lock이 자동으로 해제되지 않으므로
     * 별도의 명령어를 사용하거나 선점시간이 종료되어 수동 해제처리를 해줘야한다..
     * 2. 데이터 소스를 분리하지 않고 하나로 사용할 경우 커넥션 풀이 부족해질 수 있다.
     * */
    //@Transactional(propagation = Propagation.REQUIRES_NEW) // 하나의 스레드에 둘 이상의 트랜잭션을 가짐
    @Transactional
    public void ticketing(long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new IllegalArgumentException("Ticket Not Found."));
        ticket.increaseReservedAmount();
        int ticketNumber = ticket.getReservedAmount();
        ticketReservationHistoryRepository.save(new TicketReservationHistory(ticket, ticketNumber));
    }
}

// saveandFlush , synchronized
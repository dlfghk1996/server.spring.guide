package server.spring.guide.thread.concurrency.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import server.spring.guide.thread.concurrency.repository.NamedLockTicketRepository;


@RequiredArgsConstructor
@Component
public class NamedLockTicketFacade {

    private final NamedLockTicketRepository namedLockTicketRepository;
    private final NamedLockTicketService namedLockTicketService;

    @Transactional
    public void ticketing(Long id) {

        try {

            /*GET_LOCK(String, timeout)
            * 입력받은 이름(String)으로 timeout(단위: 초) 동안 잠금 획득을 시도한다.
            * timeout에 음수를 입력하면 잠금을 획득할 때 까지 무한대기하게 된다.
            * 한 세션에서 잠금을 유지하고 있는 동안에는 다른 세션(=thread)에서 동일한 이름의 잠금을 획득할 수 없다.
            * GET_LOCK()을 이용하여 획득한 잠금은 트랜잭션(Transaction)이 커밋(Commit)되거나 롤백(Rollback)되어도 해제되지 않는다.
            * GET_LOCK()의 결괏값은 1(성공), 0(실패), null(에러발생)을 반환한다.*/
            int lockResult = namedLockTicketRepository.getLock(Long.toString(id));
            System.out.println("lock 결과 :" + lockResult);
            namedLockTicketService.ticketing(id);
        } finally {
            /* RELEASE_LOCK(String)
            * 입력받은 이름(String)의 잠금을 해제한다.
            RELEASE_LOCK()의 결괏값은 1(성공), 0(실패), null(잠금이 존재하지 않을 때)을 반환.*/
            int result = namedLockTicketRepository.releaseLock(Long.toString(id));
            System.out.println("lock 해제 결과 :" + result);
        }
    }
}
// 분산락  : 공통된 저장소를 사용해서 자원이 사용 중인지 체크/휙득/반납하는 형태로
// 보통 분산된 서버들 간의 동기화 처리를 지원하는 락킹을 말한다.

// 분산 서버 환경에서 pessimistic lock 을 사용할 경우 ,timeout설정하는 것이 까다롭기 떄문에
// timeout 설정이 간단한 namedLock 을 사용하는게 낫다는 의견이 있다.
// namded lock 을 사용하면 복잡한 로직을 처리할 때 로직을 하나의 트랜잭션으로 묶을 수 있다는 장점이 있다.
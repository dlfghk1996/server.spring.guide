package server.spring.guide.thread.concurrency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import server.spring.guide.common.domain.Ticket;
import server.spring.guide.common.domain.UserLikeUp;

public interface NamedLockTicketRepository extends JpaRepository<Ticket, Long> {

    // GET_LOCK(lock_name, timeout)
    // timeout : 잠금을 획득할 수 없는 경우 오류를 반환하기 전 함수가 기다리는 시간을 지정
    @Query(value = "select get_lock(:key, 1000)", nativeQuery = true)
    int getLock(String key);

    @Query(value = "select release_lock(:key)", nativeQuery = true)
    int releaseLock(String key);
}

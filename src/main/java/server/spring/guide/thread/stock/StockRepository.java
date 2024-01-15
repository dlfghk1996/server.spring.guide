package server.spring.guide.thread.stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StockRepository extends JpaRepository<Stock, Long> {
    // Basic
    Stock findByProduct(String product);

    // Pessimistic Lock
  //  @Lock(LockModeType.PESSIMISTIC_WRITE)
  //  Stock findByProduct(String product);


    // Optimistic  Lock
//    @Lock(LockModeType.OPTIMISTIC)
//    Stock findByProduct(String product);


    // NamedLock
    @Query(value = "select get_lock(:key, 1000)", nativeQuery = true)
    void getLock(String key);

    @Query(value = "select release_lock(:key)", nativeQuery = true)
    void releaseLock(String key);
}

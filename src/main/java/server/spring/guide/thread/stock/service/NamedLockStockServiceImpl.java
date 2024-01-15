package server.spring.guide.thread.stock.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import server.spring.guide.thread.stock.Stock;
import server.spring.guide.thread.stock.StockRepository;
import server.spring.guide.thread.stock.StockService;

@Slf4j
@RequiredArgsConstructor
@Service
public class NamedLockStockServiceImpl implements StockService {
    private final StockRepository stockRepository;


    /* Named Lock (낙관적 락) : MetaData 단위에 대한 Lock
    * 테이블 단위로 Lock 휙득 - MySQL 서버 메모리 직접 동작
    * 이름을 가진 Lock을 휙득하여 해제할 때까지 다른 세션은 해당 Lock 을 휙득할 수 없다.
    * 분산락 구현 (MySQL Named Lock: GET_LOCK, RELEASE_LOCK)
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW) // 하나의 스레드에 둘 이상의 트랜잭션을 가짐
    public synchronized Long decreaseQuantity(String product, int quantity) {
        Stock stock = stockRepository.findByProduct(product);
        stock.decrease(quantity);
        stock = stockRepository.saveAndFlush(stock);
        return stock.getId();
    }
}


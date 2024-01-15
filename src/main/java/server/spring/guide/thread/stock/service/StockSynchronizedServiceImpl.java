package server.spring.guide.thread.stock.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import server.spring.guide.thread.stock.Stock;
import server.spring.guide.thread.stock.StockRepository;
import server.spring.guide.thread.stock.StockService;

@Slf4j
@RequiredArgsConstructor
// @Transactional => 불필요한 Race Condition 을 유발시킬 수 있음
@Service
public class StockSynchronizedServiceImpl implements StockService {
    private final StockRepository stockRepository;
    /** Synchronized 키워드를 적용한 동기화 로직
     * 동기화 메서드 : 메소드 전체 영역이 임계영역
     * 임계영역 : 단 하나의 스레드만 실행할 수 있는 코드 영역
     * 스레드가 객체 내부의 동기화메소드 또는 블록에 들어가면 즉시 객체에 잠금을 걸어 다른 스레드가 임계 영역 코드를 실행하지 못하도록 한다.
     * 다른 스레드는 lock 이 걸린 객체의 모든 동기화 메소드 또는 동기화 블록을 실행할 수 없다
     * **/
    public synchronized Long decreaseQuantity(String product, int quantity) {
        // 1. get stock
        // 2. decrease stock
        // 3. save stock
        System.out.println("Thread :    " + Thread.currentThread().getName());
        Stock stock = stockRepository.findByProduct(product);
        stock.decrease(quantity);
        stockRepository.saveAndFlush(stock);
        return stock.getId();
    }
}

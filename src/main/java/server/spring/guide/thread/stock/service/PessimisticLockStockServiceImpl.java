package server.spring.guide.thread.stock.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.spring.guide.thread.stock.Stock;
import server.spring.guide.thread.stock.StockRepository;
import server.spring.guide.thread.stock.StockService;

@Slf4j
@RequiredArgsConstructor
@Service
public class PessimisticLockStockServiceImpl implements StockService {
    private final StockRepository stockRepository;

    /* Pessimistic Lock (비관적락) :  데이터에 Lock 을 건다. (Shared/Exclusive Lock을 적용)
    * 실제 데이터에 Lock 을 걸어서 정합성을 맞춘다.
    * Lock 이 해제되기 전까지 데이터를 가져갈 수 없다.
     */
    @Transactional
    public Long decreaseQuantity(String product, int quantity) {
        Stock stock = stockRepository.findByProduct(product);
        stock.decrease(quantity);
        return stock.getId();
    }
}

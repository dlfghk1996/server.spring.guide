package server.spring.guide.thread.stock.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import server.spring.guide.thread.stock.StockRepository;

@RequiredArgsConstructor
@Component
public class NamedLockStockFacade {

    private final StockRepository stockRepository;
    private final NamedLockStockServiceImpl namedLockStockService;

    public Long decreaseQuantity(String product, int quantity) {
        Long productId;
        try {
            //GET_LOCK(lock_name, timeout)
            // lock_name으로 Named Lock을 획득하려고 시도
            // timeout 매개변수는 잠금을 획득할 수 없는 경우 오류를 반환하기 전 함수가 기다리는 시간을 지정
            stockRepository.getLock(product);
            productId = namedLockStockService.decreaseQuantity(product, quantity);
        } finally {
            stockRepository.releaseLock(product);
        }
        return productId;
    }
}
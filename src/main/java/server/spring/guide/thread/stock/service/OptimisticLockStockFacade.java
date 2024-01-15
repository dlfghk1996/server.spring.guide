package server.spring.guide.thread.stock.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class OptimisticLockStockFacade {
    private final OptimisticLockStockServiceImpl optimisticLockStockService;

    /* Optimistic Lock (낙관적 락) : Version 이용
     * 1. 데이터를 읽고 update 실행 (Version up)
     * 2. version 으로 조회
     * 3. version 변경시 데이터 다시 조회
     */
    public Long decreaseQuantity(String product, int quantity) throws InterruptedException {
        Long productId;
        while (true) {
            try {
                productId = optimisticLockStockService.decreaseQuantity(product, quantity);
                break;
            } catch (Exception e) {
                // retry
                System.out.println("OPTIMISTIC LOCK VERSION CONFLICT !!!");
                System.out.println(e.getMessage());
                // 이전 version 경우 기다린다.
                Thread.sleep(1);
            }
        }
        return productId;
    }
}
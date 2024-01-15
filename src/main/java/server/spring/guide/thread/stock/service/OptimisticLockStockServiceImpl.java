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
public class OptimisticLockStockServiceImpl implements StockService {
    private final StockRepository stockRepository;
    @Transactional
    public Long decreaseQuantity(String product, int quantity) {
       Stock stock = stockRepository.findByProduct(product);
       stock.decrease(quantity);
       return stock.getId();
    }
}


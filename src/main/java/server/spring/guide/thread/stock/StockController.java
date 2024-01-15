package server.spring.guide.thread.stock;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import server.spring.guide.common.dto.UserDTO;

// 간단한 재고 시스템으로 학습하는 동시성 이슈
@Slf4j
public class StockController {
    @GetMapping
    public ResponseEntity<UserDTO> update() {
        log.debug("==================== update ====================");

        return ResponseEntity.ok().build();
    }
}

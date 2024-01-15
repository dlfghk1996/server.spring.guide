package server.spring.guide.thread.stock;


import org.springframework.stereotype.Service;


@Service
public interface StockService {


    // 멀티스레드 테스트 환경
    //결론 : 사용 방법 (주석 참고)
    //방법 1. execute(()->{}) 로 직접 함수 작성 및 실행
    //방법 2. implements Runnable, submit() 으로 custom 실행 객체 작성 및 실행
    Long decreaseQuantity(String product, int quantity);


}

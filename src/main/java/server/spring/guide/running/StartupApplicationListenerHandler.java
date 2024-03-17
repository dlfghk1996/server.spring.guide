package server.spring.guide.running;


import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

// ContextRefreshedEvent : ApplicationContext초기화되거나 Refresh 될 때 발생하는 이벤트.
// 다중 DB 설정 시 사용하기도 한다.
@Component
public class StartupApplicationListenerHandler {

    private static final org.slf4j.Logger LOG =
        LoggerFactory.getLogger(StartupApplicationListenerHandler.class);

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOG.info("ContextRefreshedEvent Event");
    }
}
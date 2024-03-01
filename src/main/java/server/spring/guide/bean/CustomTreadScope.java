package server.spring.guide.bean;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

// 사용자 정의 빈 범위
// ex. 스레드 기준
public class CustomTreadScope implements Scope {
   // 문서에서는 동기화된 맵을 사용하여 스레드로부터 안전한 방식으로 이 작업을 수행합니다.
    CustomThreadLocal customThreadLocal = new CustomThreadLocal();

    // 동기화 맵
//    private Map<String, Object> scopedObjects
//        = Collections.synchronizedMap(new HashMap<String, Object>());
//    private Map<String, Runnable> destructionCallbacks
//        = Collections.synchronizedMap(new HashMap<String, Runnable>());

    // 지정된 이름을 가진 객체 반환
    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {

        @SuppressWarnings("unchecked")
        Map<String, Object> scope = (Map<String, Object>) customThreadLocal.get();
        Object object = scope.get(name);
        if (object == null) {
            object = objectFactory.getObject();
            scope.put(name, object);
        }

        return object;
    }

    // 지정된 이름을 가진 객체 제거
    @Override
    public Object remove(String name) {
        Map<String, Object> scope = (Map<String, Object>) customThreadLocal.get();
        return scope.remove(name);
    }

    // 범위에서 지정된 객체가 소멸될 때
    // (또는 범위가 개별 객체를 파괴하지 않고 전체만 종료하는 경우 전체 범위가 소멸될 때)
    // 실행될 콜백을 등록
    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    // 지정된 이름을 가진 객체 반환
    @Override
    public String getConversationId() {
        return null;
    }

    class CustomThreadLocal extends ThreadLocal {
        protected Map<String, Object> initialValue() {
            return new HashMap<String, Object>();
        }
    }
}

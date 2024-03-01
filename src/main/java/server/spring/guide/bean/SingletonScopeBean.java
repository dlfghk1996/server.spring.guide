package server.spring.guide.bean;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SingletonScopeBean {

    @Autowired
    private ObjectProvider<PrototypeScopeBean> provider;

    @Autowired
    PrototypeScopeBean prototypeScopeBean;

    // prototype bean 을 참조 할 경우, 새 빈 인스턴스를 생성하기위함
    public PrototypeScopeBean getProto() {
        return provider.getObject();
    }

}

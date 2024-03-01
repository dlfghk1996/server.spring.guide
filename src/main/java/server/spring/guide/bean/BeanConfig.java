package server.spring.guide.bean;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import server.spring.guide.common.domain.User;
import server.spring.guide.common.dto.UserDTO;

@Configuration
public class BeanConfig {

    @Bean
    @Scope("threadScope")
    public UserBean userBean() {
        return new UserBean();
    }

    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return new CustomBeanFactoryPostProcessor();
    }
}

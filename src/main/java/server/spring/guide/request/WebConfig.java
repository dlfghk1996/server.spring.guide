package server.spring.guide.request;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import server.spring.guide.request.resolver.basic.AuthCustomArgumentResolver;
import server.spring.guide.request.resolver.convert.CustomConverterFactory;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthCustomArgumentResolver customArgumentResolver;

    // Argument Resolver 구현
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(customArgumentResolver);
    }


//    @Override
//    public void addFormatters(FormatterRegistry registry) {
//        registry.addConverter(new CustomConverter());
//    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new CustomConverterFactory());
    }
}

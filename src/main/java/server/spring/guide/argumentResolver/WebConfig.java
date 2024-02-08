package server.spring.guide.argumentResolver;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import server.spring.guide.argumentResolver.basic.AuthCustomArgumentResolver;
import server.spring.guide.argumentResolver.convert.CustomConverter;
import server.spring.guide.argumentResolver.convert.CustomConverterFactory;

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

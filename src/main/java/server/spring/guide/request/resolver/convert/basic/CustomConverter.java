package server.spring.guide.request.resolver.convert.basic;



import org.springframework.core.convert.converter.Converter;
import server.spring.guide.request.resolver.convert.enums.UserType;

/**
 * Convert<S(sourceType),T(targetType)>
 * sourceType 에서 targetType 으로 변환
 * ConverterRegistry.addConverter(Converter<?, ?> converter);
 * ConversionService : 등록되어있는 Convert 를 확인하고 등록된 Convert로 실제 Convert 작업을 수행 한다.
 * */

/**
 * 호출 시점
 * InvocableHandlerMethod -> AbstractNamedValueMethodArgumentResolver -> webDataBider
 * 메서드 파라미터 타입을 체킹하고 동일하지 않다면 Converter를 수행해서 컨트롤러 method type과 일치해주는 역할을 수행한다
 * ConverterRegistry.addConverter(Converter<?, ?> converter);
 * ConversionService : 등록되어있는 Convert 를 확인하고 등록된 Convert로 실제 Convert 작업을 수행 한다.
 * */
public class CustomConverter implements Converter<String, UserType> {
    @Override
    public UserType convert(String source) {
        return UserType.valueOf(source.trim().toUpperCase());
    }
}

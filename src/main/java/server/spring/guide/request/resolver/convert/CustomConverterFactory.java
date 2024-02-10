package server.spring.guide.request.resolver.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;


// 다양한 enum 타입에 Convert 를 적용하고 싶은 경우 ConverterFactory 사용
@SuppressWarnings({"rawtypes"})
public class CustomConverterFactory implements ConverterFactory<String, Enum> {


    @SuppressWarnings("unchecked")
    @Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnumConverter(targetType);
    }

    // Converter 구현체를 내부 정적 클래스 정의, enumType 인자로 받는다.
    private static class StringToEnumConverter<T extends Enum<T>> implements Converter<String ,T> {
        private final Class<T> enumType;

        public StringToEnumConverter(Class<T> enumType) {
            this.enumType = enumType;
        }

        //ex. String 을 enum 으로 변경하는 작업
        @Override
        public T convert(String source) {
            return Enum.valueOf(this.enumType, source.trim().toUpperCase());
        }
    }
}
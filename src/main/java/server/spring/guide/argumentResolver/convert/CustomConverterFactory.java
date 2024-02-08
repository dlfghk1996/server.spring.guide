package server.spring.guide.argumentResolver.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;


//좀 더 여러 타입에 적용하고 싶은 경우 ConverterFactory를 사용할 수 있다.

//예를 들면 Enum을 예로 들 수 있는데, 기존의 Enum은 valueOf(source)지만
// 우리는 source의 영소문자, 대문자, 좌우 빈 공백에 상관없이 적용하고 싶다.
// 그러나 매번 Enum타입에 적용하기에는 굉장히 피곤한 작업이므로 이런 경우 ConverterFactory를 활용할 수 있다.



//ex. String 을 enum 으로 변경하는 작업
@SuppressWarnings({"rawtypes"})
public class CustomConverterFactory implements ConverterFactory<String, Enum> {


    @SuppressWarnings("unchecked")
    @Override
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnumConverter(targetType);
    }

    //StringToEnumConverter를 내부 정적 클래스를 활용해 정의하는데 enumType 인자를 받도록 했다.
    // 이는 enum의 모든 타입에 대해서 Converter를 생성하기 위함이다.
    private static class StringToEnumConverter<T extends Enum<T>> implements Converter<String ,T> {
        private final Class<T> enumType;

        public StringToEnumConverter(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(String source) {
            return Enum.valueOf(this.enumType, source.trim().toUpperCase());
        }
    }
}
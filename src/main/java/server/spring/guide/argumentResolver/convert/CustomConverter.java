package server.spring.guide.argumentResolver.convert;


import org.springframework.core.convert.converter.Converter;
import server.spring.guide.argumentResolver.convert.enums.UserType;
import server.spring.guide.common.dto.UserDTO;

// Converter 용도
// 바인딩한 데이터가 일치하지 않는 경우
// 실행에 필요한 컨트롤러 메서드의 인자 타입을 맞추기 위해 바인딩한 데이터 타입을 변경하는 용도로 사용한다.
public class CustomConverter implements Converter<String, UserType> {
    @Override
    public UserType convert(String source) {
        return UserType.valueOf(source.trim().toUpperCase());
    }
}

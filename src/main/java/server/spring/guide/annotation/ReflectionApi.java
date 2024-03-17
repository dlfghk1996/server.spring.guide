package server.spring.guide.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ReflectionApi {

    // 객체가 null 검사 및 @JsonSerialized 주석 여부 검사
    public void checkIfSerializable(Object object) {
        if (Objects.isNull(object)) {
            log.info("The object to serialize is null");
        }

        Class<?> clazz = object.getClass();
        if (!clazz.isAnnotationPresent(JsonSerializable.class)) {
            log.info("The class "
                + clazz.getSimpleName()
                + " is not annotated with JsonSerializable");
        }
    }

    // 메서드에 @Init 주석 여부 검사후 필드 초기화
    public void initializeObject(Object object)  {
        Class<?> clazz = object.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Init.class)) {
                // private type access 허용
                method.setAccessible(true);
                try {
                    method.invoke(object);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    // 필드에 @JsonElement 주석 여부 검사후 JSON 문자열 생성
    public String getJsonString(Object object) {
        Class<?> clazz = object.getClass();
        Map<String, String> jsonElementsMap = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(JsonElement.class)) {
                try {
                    jsonElementsMap.put(field.getName(), (String) field.get(object));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        String jsonString = jsonElementsMap.entrySet()
            .stream()
            .map(entry -> "\"" + entry.getKey() + "\":\""
                + entry.getValue() + "\"")
            .collect(Collectors.joining(","));
        return "{" + jsonString + "}";
    }
}

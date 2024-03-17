package server.spring.guide.annotation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ObjectToJsonConverter {
    private ReflectionApi reflectionApi;
    public String convertToJson(Object object) {
        reflectionApi.checkIfSerializable(object);
        reflectionApi.initializeObject(object);
        return reflectionApi.getJsonString(object);

    }
}

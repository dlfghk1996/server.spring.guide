package server.spring.guide.basic.resolver;//package server.spring.argumentResolver;
//
//import org.springframework.core.Conventions;
//import org.springframework.core.MethodParameter;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.WebDataBinder;
//import org.springframework.web.bind.support.WebDataBinderFactory;
//import org.springframework.web.context.request.NativeWebRequest;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.method.support.ModelAndViewContainer;
//import server.spring.common.controller.dto.InitialDTO;
//
//import javax.servlet.http.HttpServletRequest;
//
//
///** HandlerMethodArgumentResolver은 컨트롤러 메서드에서 특정 조건에 맞는 파라미터가 있을 때
// * 요청에 들어온 값을 이용해 원하는 값을 바인딩해주는 인터페이스입니다.
// * ex. @RequestBody, @PathVariable
// * **/
//public class CustomArgumentResolver implements HandlerMethodArgumentResolver {
//
//   // 주어진 메서드의 파라미터가 이 Argument Resolver 에서 지원하는 타입인지 검사
//    @Override
//    public boolean supportsParameter(MethodParameter parameter) {
//        //  parameter 객체의 getParameterType() 메소드를 통해 컨트롤러 메소드의 파라미터가 UserDto 타입인지 확인한다.
//        //  그리고 일치 여부를 boolean 타입으로 반환한다.
//        return parameter.getParameterType().equals(InitialDTO.class);
//    }
//
//    // 이 메소드의 반환값이 대상이 되는 메소드의 파라미터에 바인딩된다.
//    //  실제로 바인딩을 할 객체를 리턴합니다
//    @Override
//    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
//
//        //컨트롤러에서 반복된 HTTP 헤더로부터 JWT 가져오는 로직, JWT 검증 로직, JWT 페이로드 추출 로직, 클라이언트 IP 가져오기 로직을 넣어준다.
//        //그리고 최종적으로 UserDto 를 생성해서 반환한다.
//        return new InitialDTO("resolveArgument", httpServletRequest.getRemoteAddr());
//
//        // RequestResponseBodyMethodProcessor
//        parameter = parameter.nestedIfOptional();
//        Object arg = readWithMessageConverters(webRequest, parameter, parameter.getNestedGenericParameterType());
//        String name = Conventions.getVariableNameForParameter(parameter);
//
//
//        Object arg = readWithMessageConverters(webRequest, parameter, parameter.getNestedGenericParameterType());
//        String name = Conventions.getVariableNameForParameter(parameter);
//
//        if (binderFactory != null) {
//            WebDataBinder binder = binderFactory.createBinder(webRequest, arg, name);
//            if (arg != null) {
//                validateIfApplicable(binder, parameter);
//                if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
//                    throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
//                }
//            }
//            if (mavContainer != null) {
//                mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
//            }
//        }
//
//        return adaptArgumentIfNecessary(arg, parameter);
//    }
//
//    // readwithMessageConvert() ; abstractMessageConverterMETHODargumentRESOLVER
//}

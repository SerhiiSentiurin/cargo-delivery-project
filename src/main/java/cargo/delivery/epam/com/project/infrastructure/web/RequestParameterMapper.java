package cargo.delivery.epam.com.project.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class RequestParameterMapper {
    private final ObjectMapper objectMapper;

    public <T>T handleRequest(HttpServletRequest request, Class<T> tClass){
        Map<String, String> parameters = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()){
            String nameOfParameter = parameterNames.nextElement();
            String valueOfParameter = request.getParameter(nameOfParameter);
            parameters.put(nameOfParameter, valueOfParameter);
        }
        return objectMapper.convertValue(parameters,tClass);

    }
}

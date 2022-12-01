package cargo.delivery.epam.com.project.infrastructure.web;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * This class uses ObjectMapper functionality for doing two-step conversion from given value, into instance of given
 * value type, by writing value into temporary buffer and reading from the buffer into specified target type.
 *
 * @see ObjectMapper
 */
@RequiredArgsConstructor
public class RequestParameterMapper {
    private final ObjectMapper objectMapper;

    /**
     * This method collects parameters from the request to the map and convert it with ObjectMapper to the DTO class.
     * Feature "FAIL_ON_UNKNOWN_PROPERTIES" is disabled here.
     *
     * @param request object that contains the request the client made of the servlet.
     * @param tClass object that is necessary to be built.
     * @param <T> type of object is necessary to be built.
     * @return the built object. (usually that is Data Transfer Object (DTO))
     * @see ObjectMapper
     * @see cargo.delivery.epam.com.project.infrastructure.config.servlet.FrontServletInitializer
     */
    public <T> T handleRequest(HttpServletRequest request, Class<T> tClass) {
        Map<String, String> parameters = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String nameOfParameter = parameterNames.nextElement();
            String valueOfParameter = request.getParameter(nameOfParameter);
            parameters.put(nameOfParameter, valueOfParameter);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        return objectMapper.convertValue(parameters, tClass);
    }
}

package cargo.delivery.epam.com.project.infrastructure.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Value;

import java.util.function.Function;

@Value
public class Placeholder {
    String method;
    String action;
    Function<HttpServletRequest, ModelAndView> function;
}

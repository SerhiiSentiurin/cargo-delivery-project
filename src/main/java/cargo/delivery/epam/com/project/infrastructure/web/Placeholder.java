package cargo.delivery.epam.com.project.infrastructure.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Value;

import java.util.function.Function;

/**
 * Final placeholder class which contain name of servlet method (GET, POST ect.), action (path of the request)
 * and function to convert HttpServletRequest to ModelAndView object.
 *
 * @see ProcessorRequest
 * @see ModelAndView
 * @see cargo.delivery.epam.com.project.infrastructure.config.servlet.FrontServletInitializer
 */
@Value
public class Placeholder {
    String method;
    String action;
    Function<HttpServletRequest, ModelAndView> function;
}

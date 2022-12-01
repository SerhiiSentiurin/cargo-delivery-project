package cargo.delivery.epam.com.project.infrastructure.web;

import cargo.delivery.epam.com.project.infrastructure.web.exception.AppException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Handles all income requests. Contain list of Placeholders.
 *
 * @see Placeholder
 * @see cargo.delivery.epam.com.project.infrastructure.config.servlet.FrontServletInitializer
 */
@RequiredArgsConstructor
public class ProcessorRequest {
    private final List<Placeholder> placeholders;

    /**
     * Verifies compliance request method, mapping to Placeholder's fields. Applies Placeholder's function to request.
     *
     * @param request object that contains the request the client made of the servlet.
     * @return ModelAndView object.
     * @throws AppException if request attributes mismatch Placeholder's fields.
     * @see Placeholder
     * @see ModelAndView
     */
    public ModelAndView processRequest(HttpServletRequest request) {
        ModelAndView modelAndView;
        modelAndView = placeholders.stream()
                .filter(placeholder -> placeholder.getMethod().equals(request.getMethod()))
                .filter(placeholder -> placeholder.getAction().equals(request.getHttpServletMapping().getMatchValue()))
                .findFirst()
                .map(Placeholder::getFunction)
                .map(function -> function.apply(request))
                .orElseThrow(() -> new AppException("page not found" + request.getRequestURL()));
        return modelAndView;
    }
}

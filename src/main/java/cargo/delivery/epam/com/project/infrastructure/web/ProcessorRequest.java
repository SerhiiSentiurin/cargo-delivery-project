package cargo.delivery.epam.com.project.infrastructure.web;

import cargo.delivery.epam.com.project.infrastructure.web.exception.AppException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ProcessorRequest {
    private final List<Placeholder> placeholders;

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

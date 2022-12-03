package cargo.delivery.epam.com.project.infrastructure.web;

import cargo.delivery.epam.com.project.infrastructure.web.exception.ExceptionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

/**
 * The FrontServlet is the initial contact point for handling all requests in the system. Realization of Front Controller pattern.
 */
@Log4j2
@RequiredArgsConstructor
public class FrontServlet extends HttpServlet {

    @Getter
    private final String servletName;
    private final ProcessorRequest processorRequest;
    private final ProcessorModelAndView processorModelAndView;
    private final ExceptionHandler exceptionHandler;

    /**
     * Method handling all requests in the system.
     *
     * @param req  object that contains the request the client made of the servlet.
     * @param resp object that contains the response the servlet returns to the client.
     * @throws ServletException if the HTTP request cannot be handled.
     * @throws IOException      if an input or output error occurs while the servlet is handling the HTTP request.
     * @see HttpServlet
     * @see ModelAndView
     * @see ProcessorRequest
     * @see ProcessorModelAndView
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Start processing request " + "'" + req.getRequestURI() + "'" + " method: " + "'" + req.getMethod() + "'");
        ModelAndView modelAndView;
        try {
            modelAndView = processorRequest.processRequest(req);
        } catch (Exception e) {
            modelAndView = exceptionHandler.handle(e);
        }
        processorModelAndView.createModelAndView(req, resp, modelAndView, this);
        log.info("Request processed!");
    }
}

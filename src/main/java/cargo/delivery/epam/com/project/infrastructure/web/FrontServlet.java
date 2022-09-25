package cargo.delivery.epam.com.project.infrastructure.web;

import cargo.delivery.epam.com.project.infrastructure.web.exception.ExceptionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
@Log4j2
@RequiredArgsConstructor
public class FrontServlet extends HttpServlet {

    private final String servletName;
    private final ProcessorRequest processorRequest;
    private final ProcessorModelAndView processorModelAndView;
    private final ExceptionHandler exceptionHandler;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("start processing request");
        ModelAndView modelAndView;
        try {
            modelAndView = processorRequest.processRequest(req);
        }catch (Exception e){
            modelAndView = exceptionHandler.handle(e);
        }
        processorModelAndView.crateModelAndView(req,resp,modelAndView,this);
    }
}

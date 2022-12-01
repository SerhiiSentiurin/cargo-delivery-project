package cargo.delivery.epam.com.project.infrastructure.web.exception;

import cargo.delivery.epam.com.project.infrastructure.web.ModelAndView;
import cargo.delivery.epam.com.project.infrastructure.web.FrontServlet;

/**
 * Exception handler for all types of exceptions
 */
public class ExceptionHandler {
    /**
     * Checks caught exception for type of AppException, if it is not returns InternalError page.
     * It handles caught exception in FrontServlet class.
     *
     * @param exception all type of exceptions
     * @return ModelAndView with message in attributes and view is error page.
     * @see AppException
     * @see ModelAndView
     * @see FrontServlet
     */
    public ModelAndView handle(Exception exception) {
        if (exception instanceof AppException) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setView("/error/error.jsp");
            modelAndView.addAttribute("message", exception.getMessage());
            return modelAndView;
        }
        return ModelAndView.withView("/error/internalError.jsp");
    }
}

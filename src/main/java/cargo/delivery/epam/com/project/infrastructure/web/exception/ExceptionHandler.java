package cargo.delivery.epam.com.project.infrastructure.web.exception;

import cargo.delivery.epam.com.project.infrastructure.web.ModelAndView;

public class ExceptionHandler {
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

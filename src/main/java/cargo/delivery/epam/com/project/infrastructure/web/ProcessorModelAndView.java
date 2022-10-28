package cargo.delivery.epam.com.project.infrastructure.web;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ProcessorModelAndView {
    public void crateModelAndView(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView, HttpServlet servlet) throws IOException, ServletException {
        if (modelAndView.isRedirect()) {
            String view = modelAndView.getView();
            response.sendRedirect(request.getContextPath() + view);
            return;
        }
        RequestDispatcher requestDispatcher = servlet.getServletContext().getRequestDispatcher(modelAndView.getView());
        fillAttributes(request, modelAndView);
        requestDispatcher.forward(request, response);
    }

    private void fillAttributes(HttpServletRequest request, ModelAndView modelAndView) {
        modelAndView.getAttributes().forEach(request::setAttribute);
    }
}

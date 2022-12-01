package cargo.delivery.epam.com.project.infrastructure.web;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * This is realization of post-redirect-get (PRG) pattern.
 */
public class ProcessorModelAndView {
    /**
     * Method identify what redirection type apply to (forward or sendRedirect) depending on "isRedirect" flag in ModelAndView object.
     *
     * @param request object that contains the request the client made of the servlet.
     * @param response object that contains the response the servlet returns to the client.
     * @param modelAndView object that contains map with attributes and isRedirect flag.
     * @param servlet HttpServlet implementation class.
     * @throws IOException If an input or output exception occurs
     * @throws ServletException if the target resource throws this exception
     *
     * @see ModelAndView
     * @see FrontServlet
     */
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

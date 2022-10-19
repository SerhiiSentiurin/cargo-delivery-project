package cargo.delivery.epam.com.project.infrastructure.web.filter.security;

import cargo.delivery.epam.com.project.logic.entity.User;
import cargo.delivery.epam.com.project.logic.entity.UserRole;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SecurityFilter implements Filter {

    private List<PathMatcher> pathMatchers;

    @Override
    public void init(FilterConfig filterConfig) {
        List<PathMatcher> pathMatchers = new ArrayList<>();

        pathMatchers.add(new PathMatcher("/manager/managerHome.jsp", UserRole.MANAGER));
        pathMatchers.add(new PathMatcher("/cargo/client/getWalletInfo", UserRole.CLIENT));
        pathMatchers.add(new PathMatcher("/cargo/client/getClientOrders", UserRole.CLIENT));
        pathMatchers.add(new PathMatcher("/cargo/client/routes", UserRole.CLIENT));
        pathMatchers.add(new PathMatcher("/cargo/client/calculateDelivery", UserRole.CLIENT));
        pathMatchers.add(new PathMatcher("/cargo/client/getInvoice", UserRole.CLIENT));
        pathMatchers.add(new PathMatcher("/cargo/client/getAllOrders/filter", UserRole.CLIENT));
        pathMatchers.add(new PathMatcher("/client/clientHome.jsp", UserRole.CLIENT));
        pathMatchers.add(new PathMatcher("/client/clientInvoice.jsp", UserRole.CLIENT));
        pathMatchers.add(new PathMatcher("/client/clientOrders.jsp", UserRole.CLIENT));
        pathMatchers.add(new PathMatcher("/client/clientWallet.jsp", UserRole.CLIENT));
        pathMatchers.add(new PathMatcher("/client/getOrder.jsp", UserRole.CLIENT));
        pathMatchers.add(new PathMatcher("/cargo/manager/getAllOrders", UserRole.MANAGER));
        pathMatchers.add(new PathMatcher("/cargo/manager/getNotConfirmedOrders", UserRole.MANAGER));
        pathMatchers.add(new PathMatcher("/cargo/manager/getAllOrders/filter", UserRole.MANAGER));
        pathMatchers.add(new PathMatcher("/cargo/manager/getReport", UserRole.MANAGER));
        pathMatchers.add(new PathMatcher("/manager/allOrders.jsp", UserRole.MANAGER));
        pathMatchers.add(new PathMatcher("/manager/manageOrders.jsp", UserRole.MANAGER));
        pathMatchers.add(new PathMatcher("/manager/managerHome.jsp", UserRole.MANAGER));
        pathMatchers.add(new PathMatcher("/view/header.jsp", UserRole.MANAGER, UserRole.CLIENT));

        this.pathMatchers = pathMatchers;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String pathWithoutContext = getPathWithoutContext(request);

        Boolean hasAccess = pathMatchers.stream()
                .filter(pathMatcher -> pathMatcher.isMatchPath(pathWithoutContext))
                .findFirst()
                .map(pathMatcher -> hasRole(pathMatcher, request))
                .orElse(true);

        if (hasAccess) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/error/forbidden.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    public void destroy() {
        pathMatchers.clear();
    }

    private String getPathWithoutContext(HttpServletRequest httpServletRequest) {
        int contextPathLength = httpServletRequest.getContextPath().length();
        return httpServletRequest.getRequestURI().substring(contextPathLength);
    }

    private boolean hasRole(PathMatcher pathMatcher, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && pathMatcher.hasRole((User) session.getAttribute("user"));
    }
}

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

/**
 * Implementation of Filter. Сompares user roles with the paths allowed to them.
 *
 * @see Filter
 */
public class SecurityFilter implements Filter {

    private List<PathMatcher> pathMatchers;

    /**
     * Called by the web container to indicate to a filter that it is being placed into service.
     * The servlet container calls the init method exactly once after instantiating the filter.
     * The init method must complete successfully before the filter is asked to do any filtering work.
     * During this method is executing, the user roles are initialized with the requests they are allowed.
     *
     * @param filterConfig The configuration information associated with the filter instance being initialised
     * @see PathMatcher
     * @see UserRole
     */
    @Override
    public void init(FilterConfig filterConfig) {
        List<PathMatcher> pathMatchers = new ArrayList<>();

        pathMatchers.add(new PathMatcher("/manager/home.jsp", UserRole.MANAGER));
        pathMatchers.add(new PathMatcher("/cargo/client/wallet", UserRole.CLIENT));
        pathMatchers.add(new PathMatcher("/cargo/client/orders", UserRole.CLIENT));
        pathMatchers.add(new PathMatcher("/cargo/client/routes", UserRole.CLIENT));
        pathMatchers.add(new PathMatcher("/cargo/client/calculate/delivery", UserRole.CLIENT));
        pathMatchers.add(new PathMatcher("/cargo/client/invoice", UserRole.CLIENT));
        pathMatchers.add(new PathMatcher("/cargo/client/orders/filter", UserRole.CLIENT));
        pathMatchers.add(new PathMatcher("/client/home.jsp", UserRole.CLIENT));
        pathMatchers.add(new PathMatcher("/client/invoice.jsp", UserRole.CLIENT));
        pathMatchers.add(new PathMatcher("/client/orders.jsp", UserRole.CLIENT));
        pathMatchers.add(new PathMatcher("/client/wallet.jsp", UserRole.CLIENT));
        pathMatchers.add(new PathMatcher("/client/order.jsp", UserRole.CLIENT));
        pathMatchers.add(new PathMatcher("/cargo/manager/orders", UserRole.MANAGER));
        pathMatchers.add(new PathMatcher("/cargo/manager/orders/notConfirmed", UserRole.MANAGER));
        pathMatchers.add(new PathMatcher("/cargo/manager/orders/filter", UserRole.MANAGER));
        pathMatchers.add(new PathMatcher("/cargo/manager/report", UserRole.MANAGER));
        pathMatchers.add(new PathMatcher("/manager/allOrders.jsp", UserRole.MANAGER));
        pathMatchers.add(new PathMatcher("/manager/manageOrders.jsp", UserRole.MANAGER));
        pathMatchers.add(new PathMatcher("/manager/home.jsp", UserRole.MANAGER));
        pathMatchers.add(new PathMatcher("/view/header.jsp", UserRole.MANAGER, UserRole.CLIENT));

        this.pathMatchers = pathMatchers;
    }

    /**
     * The doFilter method of the Filter is called by the container each time a request/response pair
     * is passed through the chain due to a client request for a resource at the end of the chain.
     * The FilterChain passed in to this method allows the Filter to pass on the request and response
     * to the next entity in the chain.
     * This method permit to user access if it permitted in PathMatcher, else forward to forbidden page.
     *
     * @param servletRequest  The request to process (converted to HttpServletRequest)
     * @param servletResponse The response associated with the request (converted to HttpServletResponse)
     * @param filterChain     Provides access to the next filter in the chain for this filter to pass the request and response to for further processing
     * @throws IOException      if an I/O error occurs during this filter's processing of the request
     * @throws ServletException if the processing fails for any other reason
     * @see PathMatcher
     */
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

package cargo.delivery.epam.com.project.logic.controllers;

import cargo.delivery.epam.com.project.infrastructure.web.ModelAndView;
import cargo.delivery.epam.com.project.infrastructure.web.RequestParameterMapper;
import cargo.delivery.epam.com.project.logic.entity.Route;
import cargo.delivery.epam.com.project.logic.entity.dto.ClientOrderDto;
import cargo.delivery.epam.com.project.logic.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final RequestParameterMapper requestParameterMapper;

    // /app/cargo/client/routes
    public ModelAndView getRoutesForRegisterUser(HttpServletRequest request) {
        ModelAndView modelAndView = viewWithRoutes();
        modelAndView.setView("/client/getOrder.jsp");
        return modelAndView;
    }

    // /app/cargo/routes
    public ModelAndView getRoutesForNonRegisterUser(HttpServletRequest request) {
        ModelAndView modelAndView = viewWithRoutes();
        modelAndView.setView("/all/getCost.jsp");
        return modelAndView;
    }

    private ModelAndView viewWithRoutes() {
        List<Route> senderCities = orderService.getDistinctSenderCities();
        List<Route> recipientCities = orderService.getDistinctRecipientCities();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addAttribute("routeSender", senderCities);
        modelAndView.addAttribute("routeRecipient", recipientCities);
        return modelAndView;
    }

    // /app/cargo/calculateDelivery
    public ModelAndView getDeliveryCostForNotRegisteredUser(HttpServletRequest request) {
        ModelAndView modelAndView = viewWithOrders(request);
        modelAndView.setView("/cargo/routes");
        return modelAndView;
    }

    // /app/cargo/client/calculateDelivery
    public ModelAndView getDeliveryCostForRegisteredUser(HttpServletRequest request) {

        ModelAndView modelAndView = viewWithOrders(request);
        modelAndView.setView("/cargo/client/routes");
        return modelAndView;
    }

    private ModelAndView viewWithOrders(HttpServletRequest request) {
        ClientOrderDto dto = requestParameterMapper.handleRequest(request, ClientOrderDto.class);
        ClientOrderDto newOrderDto = orderService.calculateDeliveryCost(dto);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addAttribute("order", newOrderDto);
        return modelAndView;
    }

    // app/cargo/client/createOrder
    public ModelAndView createOrder(HttpServletRequest request) {
        ClientOrderDto dto = requestParameterMapper.handleRequest(request, ClientOrderDto.class);
        int page = Integer.parseInt(request.getParameter("page"));
        ModelAndView modelAndView = ModelAndView.withView("/cargo/client/getClientOrders?clientId=" + dto.getClientId()+"&page="+page);
        orderService.createOrder(dto);
        modelAndView.setRedirect(true);
        return modelAndView;
    }


}

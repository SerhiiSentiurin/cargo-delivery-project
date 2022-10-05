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

    // /app/cargo/getInfoToOder
    // /app/cargo/client/getInfoToOder
    public ModelAndView getInfoToOder(HttpServletRequest request) {
        List<Route> senderCities = orderService.getDistinctSenderCities();
        List<Route> recipientCities = orderService.getDistinctRecipientCities();
        String userId = request.getParameter("userId");
        ModelAndView modelAndView = new ModelAndView();
        if (userId.isEmpty()){
            modelAndView.setView("/all/getCost.jsp");
        }else {
            modelAndView.setView("/client/getOrder.jsp");
        }
        modelAndView.addAttribute("routeSender", senderCities);
        modelAndView.addAttribute("routeRecipient", recipientCities);
        return modelAndView;
    }

    // /app/cargo/calculateDelivery
    // /app/cargo/client/calculateDelivery
    public ModelAndView getDeliveryCost(HttpServletRequest request){
        ClientOrderDto dto = requestParameterMapper.handleRequest(request,ClientOrderDto.class);
        String userId = request.getParameter("userId");
        ClientOrderDto newOrderDto = orderService.calculateDeliveryCost(dto);
        ModelAndView modelAndView;
        if (userId.isEmpty()){
            modelAndView = ModelAndView.withView("/cargo/getInfoToOder");
        }else {
            modelAndView = ModelAndView.withView("/cargo/client/getInfoToOder");
        }
        modelAndView.addAttribute("order", newOrderDto);
        return modelAndView;
    }

    // app/cargo/client/createOrder
    public ModelAndView createOrder(HttpServletRequest request){
        ClientOrderDto dto = requestParameterMapper.handleRequest(request,ClientOrderDto.class);


        ModelAndView modelAndView = ModelAndView.withView("/cargo/client/getInfoToOder");

        return modelAndView;
    }


}

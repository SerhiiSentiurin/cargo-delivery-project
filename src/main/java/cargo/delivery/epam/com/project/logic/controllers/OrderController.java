package cargo.delivery.epam.com.project.logic.controllers;

import cargo.delivery.epam.com.project.infrastructure.web.ModelAndView;
import cargo.delivery.epam.com.project.infrastructure.web.RequestParameterMapper;
import cargo.delivery.epam.com.project.logic.entity.Route;
import cargo.delivery.epam.com.project.logic.entity.dto.OrderDto;
import cargo.delivery.epam.com.project.logic.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final RequestParameterMapper requestParameterMapper;

    // /app/cargo/chooseCargoParameters
    public ModelAndView getAllRoutes(HttpServletRequest request) {

        List<Route> senderCities = orderService.getDistinctSenderCities();
        List<Route> recipientCities = orderService.getDistinctRecipientCities();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/all/getCost.jsp");
        modelAndView.addAttribute("routeSender", senderCities);
        modelAndView.addAttribute("routeRecipient", recipientCities);
        return modelAndView;
    }

    public ModelAndView getDeliveryCost(HttpServletRequest request){
        String senderCity = request.getParameter("senderCity");
        String recipientCity = request.getParameter("recipientCity");
        Double weight = Double.parseDouble(request.getParameter("weight"));
        Double volume = Double.parseDouble(request.getParameter("volume"));
        OrderDto order = orderService.getDeliveryCost(senderCity,recipientCity,weight,volume);
        ModelAndView modelAndView = ModelAndView.withView("/cargo/chooseCargoParameters?senderCity="+senderCity+"&recipientCity="+recipientCity+"&weight="+weight+"&volume="+volume);
        modelAndView.addAttribute("order", order);
        return modelAndView;
    }


}

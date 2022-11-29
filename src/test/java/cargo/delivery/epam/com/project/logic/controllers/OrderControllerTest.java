package cargo.delivery.epam.com.project.logic.controllers;

import cargo.delivery.epam.com.project.infrastructure.web.ModelAndView;
import cargo.delivery.epam.com.project.infrastructure.web.RequestParameterMapper;
import cargo.delivery.epam.com.project.logic.entity.Route;
import cargo.delivery.epam.com.project.logic.entity.dto.ClientOrderDto;
import cargo.delivery.epam.com.project.logic.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {
    @Mock
    OrderService orderService;
    @Mock
    RequestParameterMapper requestParameterMapper;
    @Mock
    HttpServletRequest request;

    @InjectMocks
    OrderController orderController;

    private static final Route sender1 = Mockito.mock(Route.class);
    private static final Route sender2 = Mockito.mock(Route.class);
    private static final Route recipient1 = Mockito.mock(Route.class);
    private static final Route recipient2 = Mockito.mock(Route.class);
    private static final int PAGE = 1;

    @Test
    public void getRoutesForRegisteredUserTest() {
        List<Route> expSenderCities = new ArrayList<>();
        expSenderCities.add(sender1);
        expSenderCities.add(sender2);
        List<Route> expRecipientCities = new ArrayList<>();
        expRecipientCities.add(recipient1);
        expRecipientCities.add(recipient2);
        when(orderService.getDistinctSenderCities()).thenReturn(expSenderCities);
        when(orderService.getDistinctRecipientCities()).thenReturn(expRecipientCities);

        ModelAndView modelAndView = orderController.getRoutesForRegisterUser(request);
        assertNotNull(modelAndView);
        assertEquals("/client/order.jsp", modelAndView.getView());
        assertEquals(expSenderCities, modelAndView.getAttributes().get("routeSender"));
        assertEquals(expRecipientCities, modelAndView.getAttributes().get("routeRecipient"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void getRoutesForNonRegisteredUserTest() {
        List<Route> expSenderCities = new ArrayList<>();
        expSenderCities.add(sender1);
        expSenderCities.add(sender2);
        List<Route> expRecipientCities = new ArrayList<>();
        expRecipientCities.add(recipient1);
        expRecipientCities.add(recipient2);
        when(orderService.getDistinctSenderCities()).thenReturn(expSenderCities);
        when(orderService.getDistinctRecipientCities()).thenReturn(expRecipientCities);

        ModelAndView modelAndView = orderController.getRoutesForNonRegisterUser(request);
        assertNotNull(modelAndView);
        assertEquals("/notRegistered/getCost.jsp", modelAndView.getView());
        assertEquals(expSenderCities, modelAndView.getAttributes().get("routeSender"));
        assertEquals(expRecipientCities, modelAndView.getAttributes().get("routeRecipient"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void getDeliveryCostForNotRegisteredUserTest() {
        ClientOrderDto dto = new ClientOrderDto();
        ClientOrderDto dtoWithCost = new ClientOrderDto();
        dtoWithCost.setDeliveryCost(3500d);
        when(requestParameterMapper.handleRequest(request, ClientOrderDto.class)).thenReturn(dto);
        when(orderService.calculateDeliveryCost(dto)).thenReturn(dtoWithCost);

        ModelAndView modelAndView = orderController.getDeliveryCostForNotRegisteredUser(request);
        assertNotNull(modelAndView);
        assertEquals("/cargo/routes", modelAndView.getView());
        assertEquals(dtoWithCost, modelAndView.getAttributes().get("order"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void getDeliveryCostForRegisteredUserTest() {
        ClientOrderDto dto = new ClientOrderDto();
        ClientOrderDto dtoWithCost = new ClientOrderDto();
        dtoWithCost.setDeliveryCost(3500d);
        when(requestParameterMapper.handleRequest(request, ClientOrderDto.class)).thenReturn(dto);
        when(orderService.calculateDeliveryCost(dto)).thenReturn(dtoWithCost);

        ModelAndView modelAndView = orderController.getDeliveryCostForRegisteredUser(request);
        assertNotNull(modelAndView);
        assertEquals("/cargo/client/routes", modelAndView.getView());
        assertEquals(dtoWithCost, modelAndView.getAttributes().get("order"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void createOrderTest() {
        ClientOrderDto dto = Mockito.mock(ClientOrderDto.class);
        when(requestParameterMapper.handleRequest(request, ClientOrderDto.class)).thenReturn(dto);
        when(request.getParameter("page")).thenReturn(String.valueOf(PAGE));

        ModelAndView modelAndView = orderController.createOrder(request);
        assertNotNull(modelAndView);
        assertEquals("/cargo/client/orders?clientId=0&page=1", modelAndView.getView());
        assertTrue(modelAndView.isRedirect());

        verify(orderService).createOrder(dto);
    }
}

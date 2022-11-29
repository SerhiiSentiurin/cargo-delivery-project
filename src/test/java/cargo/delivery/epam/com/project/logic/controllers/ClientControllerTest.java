package cargo.delivery.epam.com.project.logic.controllers;

import cargo.delivery.epam.com.project.infrastructure.web.ModelAndView;
import cargo.delivery.epam.com.project.infrastructure.web.RequestParameterMapper;
import cargo.delivery.epam.com.project.infrastructure.web.pagination.PaginationLinksBuilder;
import cargo.delivery.epam.com.project.logic.entity.Client;
import cargo.delivery.epam.com.project.logic.entity.Order;
import cargo.delivery.epam.com.project.logic.entity.Report;
import cargo.delivery.epam.com.project.logic.entity.dto.ClientCreateDto;
import cargo.delivery.epam.com.project.logic.entity.dto.FilteringDto;
import cargo.delivery.epam.com.project.logic.services.ClientService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClientControllerTest {
    @Mock
    ClientService clientService;
    @Mock
    RequestParameterMapper requestParameterMapper;
    @Mock
    PaginationLinksBuilder paginationLinksBuilder;
    @Mock
    HttpServletRequest request;

    @InjectMocks
    ClientController clientController;

    private static final Long CLIENT_ID = 2L;
    private static final Long ORDER_ID = 1L;
    private static final int PAGE = 1;
    private static final Report report1 = Mockito.mock(Report.class);
    private static final Report report2 = Mockito.mock(Report.class);
    private static final int COUNT_PAGES = 5;

    @Test
    public void createNewClientTest() {
        ClientCreateDto dto = Mockito.mock(ClientCreateDto.class);
        when(requestParameterMapper.handleRequest(request, ClientCreateDto.class)).thenReturn(dto);
        ModelAndView modelAndView = clientController.createNewClient(request);
        assertNotNull(modelAndView);
        assertEquals("/client/successRegistration.jsp", modelAndView.getView());
        assertTrue(modelAndView.isRedirect());
    }

    @Test
    public void getWalletInfoTest() {
        Client client = new Client();
        client.setId(CLIENT_ID);
        when(request.getParameter("clientId")).thenReturn(String.valueOf(CLIENT_ID));
        when(clientService.getClientById(CLIENT_ID)).thenReturn(client);

        ModelAndView modelAndView = clientController.getWalletInfo(request);
        assertNotNull(modelAndView);
        assertEquals("/client/wallet.jsp", modelAndView.getView());
        assertEquals(client, modelAndView.getAttributes().get("client"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void topUpClientWalletTest() {
        when(request.getParameter("amount")).thenReturn(String.valueOf(100d));
        when(request.getParameter("clientId")).thenReturn(String.valueOf(CLIENT_ID));

        ModelAndView modelAndView = clientController.topUpClientWallet(request);
        assertNotNull(modelAndView);
        assertEquals("/cargo/client/wallet?clientId=" + CLIENT_ID, modelAndView.getView());
        assertTrue(modelAndView.isRedirect());
    }

    @Test
    public void getClientOrdersTest() {
        List<Report> expList = new ArrayList<>();
        expList.add(report1);
        expList.add(report2);
        List<String> pagLinks = new ArrayList<>();
        pagLinks.add("link");

        when(request.getParameter("clientId")).thenReturn(String.valueOf(CLIENT_ID));
        when(request.getParameter("page")).thenReturn(String.valueOf(PAGE));
        when(clientService.getOrdersByClientId(CLIENT_ID, PAGE)).thenReturn(expList);
        when(clientService.getCountPagesAllOrdersClient(CLIENT_ID)).thenReturn(COUNT_PAGES);
        when(paginationLinksBuilder.buildLinks(request, COUNT_PAGES)).thenReturn(pagLinks);

        ModelAndView modelAndView = clientController.getClientOrders(request);
        assertNotNull(modelAndView);
        assertEquals("/client/orders.jsp", modelAndView.getView());
        assertEquals(expList, modelAndView.getAttributes().get("reports"));
        assertEquals(pagLinks, modelAndView.getAttributes().get("paginationLinks"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void getOrderForInvoiceTest() {
        Order order = Mockito.mock(Order.class);
        LocalDate date = LocalDate.now();

        when(request.getParameter("orderId")).thenReturn(String.valueOf(ORDER_ID));
        when(clientService.getOrderForInvoice(ORDER_ID)).thenReturn(order);

        ModelAndView modelAndView = clientController.getOrderForInvoice(request);
        assertNotNull(modelAndView);
        assertEquals("/client/invoice.jsp", modelAndView.getView());
        assertEquals(order, modelAndView.getAttributes().get("order"));
        assertEquals(date, modelAndView.getAttributes().get("date"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void payInvoiceTest() {
        when(request.getParameter("orderId")).thenReturn(String.valueOf(ORDER_ID));
        when(request.getParameter("clientId")).thenReturn(String.valueOf(CLIENT_ID));
        when(request.getParameter("page")).thenReturn(String.valueOf(PAGE));
        when(request.getParameter("arrivalDate")).thenReturn("2000-01-03");
        ModelAndView modelAndView = clientController.payInvoice(request);
        assertNotNull(modelAndView);
        assertEquals("/cargo/client/orders?clientId=" + CLIENT_ID + "&page=" + PAGE, modelAndView.getView());
        assertTrue(modelAndView.isRedirect());
    }

    @Test
    public void filterOrdersTest() {
        FilteringDto dto = Mockito.mock(FilteringDto.class);
        List<Report> expReportList = new ArrayList<>();
        expReportList.add(report1);
        expReportList.add(report2);
        List<String> expPagLinks = new ArrayList<>();
        expPagLinks.add("link");
        when(requestParameterMapper.handleRequest(request, FilteringDto.class)).thenReturn(dto);
        when(clientService.filterOrders(dto)).thenReturn(expReportList);
        when(clientService.getCountPagesFiltered(dto)).thenReturn(COUNT_PAGES);
        when(paginationLinksBuilder.buildLinks(request, COUNT_PAGES)).thenReturn(expPagLinks);
        ModelAndView modelAndView = clientController.filterOrders(request);
        assertNotNull(modelAndView);
        assertEquals("/client/orders.jsp", modelAndView.getView());
        assertEquals(expReportList, modelAndView.getAttributes().get("reports"));
        assertEquals(expPagLinks, modelAndView.getAttributes().get("paginationLinks"));
        assertFalse(modelAndView.isRedirect());
    }
}

package cargo.delivery.epam.com.project.logic.services;

import cargo.delivery.epam.com.project.infrastructure.web.exception.AppException;
import cargo.delivery.epam.com.project.logic.dao.ClientDAO;
import cargo.delivery.epam.com.project.logic.dao.ReportFilteringDAO;
import cargo.delivery.epam.com.project.logic.entity.Client;
import cargo.delivery.epam.com.project.logic.entity.Invoice;
import cargo.delivery.epam.com.project.logic.entity.Order;
import cargo.delivery.epam.com.project.logic.entity.Report;
import cargo.delivery.epam.com.project.logic.entity.dto.ClientCreateDto;
import cargo.delivery.epam.com.project.logic.entity.dto.FilteringDto;
import liquibase.pro.packaged.O;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClientServiceTest {
    @Mock
    ClientDAO clientDAO;
    @Mock
    ReportFilteringDAO reportFilteringDAO;

    @InjectMocks
    ClientService clientService;

    private static final FilteringDto filterDto = Mockito.mock(FilteringDto.class);
    private static final ClientCreateDto clientDto = Mockito.mock(ClientCreateDto.class);
    private static final double COUNT_OF_ROWS = 30d;
    private static final int PAGE = 3;
    private static final int ORDERS_PER_PAGE = 10;
    private static final Long CLIENT_ID = 2L;
    private static final Long ORDER_ID = 1L;

    @Test
    public void getCountPagesFilteredTest() {
        when(reportFilteringDAO.getCountFilteredRows(filterDto)).thenReturn(COUNT_OF_ROWS);
        int resultCount = clientService.getCountPagesFiltered(filterDto);
        assertEquals(PAGE, resultCount);
    }

    @Test
    public void getCountPagesAllOrdersClientTest() {
        when(clientDAO.getCountOfRowsAllOrdersClient(CLIENT_ID)).thenReturn(COUNT_OF_ROWS);
        int resultCount = clientService.getCountPagesAllOrdersClient(CLIENT_ID);
        assertEquals(PAGE, resultCount);
    }

    @Test
    public void createNewClientTest() {
        clientService.createNewClient(clientDto);
        verify(clientDAO).createNewClient(clientDto);
    }

    @Test
    public void getClientByIdTest() {
        Client expectedClient = Mockito.mock(Client.class);
        when(reportFilteringDAO.getClientById(CLIENT_ID)).thenReturn(expectedClient);
        Client resultClient = clientService.getClientById(CLIENT_ID);
        assertEquals(expectedClient, resultClient);
        assertNotNull(resultClient);
        verify(reportFilteringDAO).getClientById(CLIENT_ID);
    }

    @Test
    public void topUpClientWalletTest() {
        Client client = Mockito.mock(Client.class);
        when(reportFilteringDAO.getClientById(CLIENT_ID)).thenReturn(client);
        clientService.topUpClientWallet(10d, CLIENT_ID);
        verify(clientDAO).topUpClientWallet(10d, CLIENT_ID, client.getAmount());
    }

    @Test
    public void getOrdersByClientIdTest() {
        int index = (PAGE - 1) * ORDERS_PER_PAGE;
        Order order = Mockito.mock(Order.class);
        Client client = Mockito.mock(Client.class);
        Report report = new Report(client, order);
        List<Report> expectedList = new ArrayList<>();
        expectedList.add(report);

        when(clientDAO.getReportsByClientId(CLIENT_ID, index)).thenReturn(expectedList);

        List<Report> resultList = clientService.getOrdersByClientId(CLIENT_ID, PAGE);
        assertEquals(expectedList, resultList);
        assertNotNull(resultList);

        verify(reportFilteringDAO).getOrderById(order.getId());
        verify(reportFilteringDAO).getClientById(client.getId());
    }

    @Test
    public void getOrdersByClientIdEmptyTest() {
        int index = (PAGE - 1) * ORDERS_PER_PAGE;
        List<Report> expectedList = new ArrayList<>();

        when(clientDAO.getReportsByClientId(CLIENT_ID, index)).thenReturn(expectedList);

        List<Report> resultList = clientService.getOrdersByClientId(CLIENT_ID, PAGE);
        assertEquals(expectedList, resultList);
        assertNotNull(resultList);
    }

    @Test
    public void getOrderForInvoiceTest() {
        Order expectedOrder = Mockito.mock(Order.class);
        when(reportFilteringDAO.getOrderById(ORDER_ID)).thenReturn(expectedOrder);
        Order resultOrder = clientService.getOrderForInvoice(ORDER_ID);
        assertEquals(expectedOrder, resultOrder);
        assertNotNull(resultOrder);
        verify(reportFilteringDAO).getOrderById(ORDER_ID);
    }

    @Test
    public void payInvoiceTest() {
        Double amountAfterPay = 0d;
        Invoice invoice = new Invoice();
        invoice.setPrice(10d);
        Client client = new Client();
        client.setId(CLIENT_ID);
        client.setAmount(10d);
        Order order = new Order();
        order.setId(ORDER_ID);
        order.setInvoice(invoice);

        when(reportFilteringDAO.getClientById(client.getId())).thenReturn(client);
        when(reportFilteringDAO.getOrderById(order.getId())).thenReturn(order);

        clientService.payInvoice(ORDER_ID, CLIENT_ID, LocalDate.of(2000, 1, 1), LocalDate.of(2000, 1, 3));
        verify(clientDAO).payInvoice(ORDER_ID, CLIENT_ID, LocalDate.of(2000, 1, 1), LocalDate.of(2000, 1, 3), amountAfterPay);
    }

    @Test(expected = AppException.class)
    public void payInvoiceWhenNotEnoughMoney() {
        Invoice invoice = new Invoice();
        invoice.setPrice(10d);
        Client client = new Client();
        client.setId(CLIENT_ID);
        client.setAmount(5d);
        Order order = new Order();
        order.setId(ORDER_ID);
        order.setInvoice(invoice);

        when(reportFilteringDAO.getClientById(client.getId())).thenReturn(client);
        when(reportFilteringDAO.getOrderById(order.getId())).thenReturn(order);
        clientService.payInvoice(ORDER_ID, CLIENT_ID, LocalDate.of(2000, 1, 1), LocalDate.of(2000, 1, 3));
    }
}

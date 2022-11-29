package cargo.delivery.epam.com.project.logic.services;

import cargo.delivery.epam.com.project.infrastructure.web.exception.AppException;
import cargo.delivery.epam.com.project.logic.dao.ManagerDAO;
import cargo.delivery.epam.com.project.logic.dao.ReportFilteringDAO;
import cargo.delivery.epam.com.project.logic.entity.Client;
import cargo.delivery.epam.com.project.logic.entity.Order;
import cargo.delivery.epam.com.project.logic.entity.Report;
import cargo.delivery.epam.com.project.logic.entity.dto.FilteringDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ManagerServiceTest {
    @Mock
    ManagerDAO managerDAO;
    @Mock
    ReportFilteringDAO reportFilteringDAO;

    @InjectMocks
    ManagerService managerService;

    private static final FilteringDto dto = Mockito.mock(FilteringDto.class);
    private static final double COUNT_OF_ROWS = 30d;
    private static final int PAGE = 3;
    private static final int ORDERS_PER_PAGE = 10;

    @Test
    public void getCountPagesFilteredTest() {
        when(reportFilteringDAO.getCountFilteredRows(dto)).thenReturn(COUNT_OF_ROWS);
        int resultCount = managerService.getCountPagesFiltered(dto);
        assertEquals(PAGE, resultCount);
    }

    @Test
    public void getCountPagesAllOrdersTest() {
        when(managerDAO.getCountOfRowsAllOrders()).thenReturn(COUNT_OF_ROWS);
        int resultCount = managerService.getCountPagesAllOrders();
        assertEquals(PAGE, resultCount);
    }

    @Test
    public void getAllOrdersTest() {
        int index = (PAGE - 1) * ORDERS_PER_PAGE;
        Order order = Mockito.mock(Order.class);
        Client client = Mockito.mock(Client.class);
        Report report = new Report(client, order);
        List<Report> expectedList = new ArrayList<>();
        expectedList.add(report);

        when(managerDAO.getAllOrders(index)).thenReturn(expectedList);

        List<Report> resultList = managerService.getAllOrders(PAGE);
        assertEquals(expectedList, resultList);

        verify(reportFilteringDAO).getOrderById(order.getId());
        verify(reportFilteringDAO).getClientById(client.getId());
    }

    @Test
    public void getAllOrdersEmptyTest() {
        int index = (PAGE - 1) * ORDERS_PER_PAGE;
        List<Report> expectedList = new ArrayList<>();

        when(managerDAO.getAllOrders(index)).thenReturn(expectedList);

        List<Report> resultList = managerService.getAllOrders(PAGE);
        assertEquals(expectedList, resultList);
        assertNotNull(resultList);
    }

    @Test
    public void getNotConfirmedOrdersTest() {
        Order order = Mockito.mock(Order.class);
        Client client = Mockito.mock(Client.class);
        Report report = new Report(client, order);
        List<Report> expectedList = new ArrayList<>();
        expectedList.add(report);

        when(managerDAO.getNotConfirmedOrders()).thenReturn(expectedList);

        List<Report> resultList = managerService.getNotConfirmedOrders();
        assertEquals(expectedList, resultList);

        verify(reportFilteringDAO).getOrderById(order.getId());
        verify(reportFilteringDAO).getClientById(client.getId());
    }

    @Test
    public void getNotConfirmedOrdersEmptyTest() {
        List<Report> expectedList = new ArrayList<>();
        when(managerDAO.getNotConfirmedOrders()).thenReturn(expectedList);
        List<Report> resultList = managerService.getNotConfirmedOrders();
        assertEquals(expectedList, resultList);
        assertNotNull(resultList);
    }

    @Test
    public void filterReportsTest() {
        Order order = Mockito.mock(Order.class);
        Client client = Mockito.mock(Client.class);
        Report report = new Report(client, order);
        List<Report> expectedList = new ArrayList<>();
        expectedList.add(report);

        when(reportFilteringDAO.filterReports(dto)).thenReturn(expectedList);

        List<Report> resultList = managerService.filterReports(dto);
        assertEquals(expectedList, resultList);

        verify(reportFilteringDAO).getOrderById(order.getId());
        verify(reportFilteringDAO).getClientById(client.getId());
    }

    @Test
    public void filterReportsEmptyTest() {
        List<Report> expectedList = new ArrayList<>();
        when(reportFilteringDAO.filterReports(dto)).thenReturn(expectedList);
        List<Report> resultList = managerService.filterReports(dto);
        assertEquals(expectedList, resultList);
        assertNotNull(resultList);
    }

    @Test
    public void getReportByDayAndDirectionTest() {
        Order order = Mockito.mock(Order.class);
        Client client = Mockito.mock(Client.class);
        Report report = new Report(client, order);
        List<Report> expectedList = new ArrayList<>();
        expectedList.add(report);
        when(managerDAO.getReportByDayAndDirection("2000-01-01", "sender", "recipient")).thenReturn(expectedList);

        List<Report> resultList = managerService.getReportByDayAndDirection("2000-01-01", "sender", "recipient");
        assertEquals(expectedList, resultList);
        assertNotNull(resultList);

        verify(reportFilteringDAO).getOrderById(order.getId());
        verify(reportFilteringDAO).getClientById(client.getId());
    }

    @Test(expected = AppException.class)
    public void getReportByDayAndDirectionEmptyTest() {
        List<Report> expectedList = new ArrayList<>();
        when(managerDAO.getReportByDayAndDirection("2000-01-01", "sender", "recipient")).thenReturn(expectedList).thenThrow(AppException.class);
        List<Report> resultList = managerService.getReportByDayAndDirection("2000-01-01", "sender", "recipient");
    }

    @Test
    public void confirmOrderTest() {
        managerDAO.confirmOrder(anyLong());
        verify(managerDAO).confirmOrder(anyLong());
    }

}

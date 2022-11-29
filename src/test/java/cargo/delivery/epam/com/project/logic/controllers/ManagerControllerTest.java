package cargo.delivery.epam.com.project.logic.controllers;

import cargo.delivery.epam.com.project.infrastructure.web.ModelAndView;
import cargo.delivery.epam.com.project.infrastructure.web.RequestParameterMapper;
import cargo.delivery.epam.com.project.infrastructure.web.pagination.PaginationLinksBuilder;
import cargo.delivery.epam.com.project.logic.entity.Report;
import cargo.delivery.epam.com.project.logic.entity.dto.FilteringDto;
import cargo.delivery.epam.com.project.logic.services.ManagerService;
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
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ManagerControllerTest {
    @Mock
    ManagerService managerService;
    @Mock
    RequestParameterMapper requestParameterMapper;
    @Mock
    PaginationLinksBuilder paginationLinksBuilder;
    @Mock
    HttpServletRequest request;

    @InjectMocks
    ManagerController managerController;

    private static final int PAGE = 1;
    private static final int COUNT_PAGES = 5;
    private static final Report report1 = Mockito.mock(Report.class);
    private static final Report report2 = Mockito.mock(Report.class);


    @Test
    public void getAllOrdersTest() {
        List<Report> expReportList = new ArrayList<>();
        expReportList.add(report1);
        expReportList.add(report2);
        List<String> expPaginationLinks = new ArrayList<>();
        expPaginationLinks.add("link");
        when(request.getParameter("page")).thenReturn(String.valueOf(PAGE));
        when(managerService.getAllOrders(PAGE)).thenReturn(expReportList);
        when(managerService.getCountPagesAllOrders()).thenReturn(COUNT_PAGES);
        when(paginationLinksBuilder.buildLinks(request, COUNT_PAGES)).thenReturn(expPaginationLinks);

        ModelAndView modelAndView = managerController.getAllOrders(request);
        assertNotNull(modelAndView);
        assertEquals("/manager/allOrders.jsp", modelAndView.getView());
        assertEquals(expReportList, modelAndView.getAttributes().get("reports"));
        assertEquals(expPaginationLinks, modelAndView.getAttributes().get("paginationLinks"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void getNotConfirmedOrdersTest() {
        List<Report> expReportList = new ArrayList<>();
        expReportList.add(report1);
        expReportList.add(report2);
        when(managerService.getNotConfirmedOrders()).thenReturn(expReportList);

        ModelAndView modelAndView = managerController.getNotConfirmedOrders(request);
        assertNotNull(modelAndView);
        assertEquals("/manager/manageOrders.jsp", modelAndView.getView());
        assertEquals(expReportList, modelAndView.getAttributes().get("reports"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void confirmOrderTest() {
        when(request.getParameter("orderId")).thenReturn(String.valueOf(1L));
        ModelAndView modelAndView = managerController.confirmOrder(request);
        assertNotNull(modelAndView);
        assertEquals("/cargo/manager/orders/notConfirmed", modelAndView.getView());
        assertTrue(modelAndView.isRedirect());
    }

    @Test
    public void filterReportsTest() {
        FilteringDto dto = Mockito.mock(FilteringDto.class);
        List<Report> expReportList = new ArrayList<>();
        expReportList.add(report1);
        expReportList.add(report2);
        List<String> pagLinks = new ArrayList<>();
        pagLinks.add("link");

        when(requestParameterMapper.handleRequest(request, FilteringDto.class)).thenReturn(dto);
        when(managerService.filterReports(dto)).thenReturn(expReportList);
        when(managerService.getCountPagesFiltered(dto)).thenReturn(COUNT_PAGES);
        when(paginationLinksBuilder.buildLinks(request, COUNT_PAGES)).thenReturn(pagLinks);

        ModelAndView modelAndView = managerController.filterReports(request);
        assertNotNull(modelAndView);
        assertEquals("/manager/allOrders.jsp", modelAndView.getView());
        assertEquals(expReportList, modelAndView.getAttributes().get("reports"));
        assertEquals(pagLinks, modelAndView.getAttributes().get("paginationLinks"));
        assertFalse(modelAndView.isRedirect());
    }

    @Test
    public void getReportByDayAndDirectionTest() {
        List<Report> expReportList = new ArrayList<>();
        expReportList.add(report1);
        expReportList.add(report2);
        when(request.getParameter("arrivalDate")).thenReturn("2000-01-01");
        when(request.getParameter("senderCity")).thenReturn("sender");
        when(request.getParameter("recipientCity")).thenReturn("recipient");
        when(managerService.getReportByDayAndDirection("2000-01-01", "sender", "recipient")).thenReturn(expReportList);

        ModelAndView modelAndView = managerController.getReportByDayAndDirection(request);
        assertNotNull(modelAndView);
        assertEquals("/manager/home.jsp", modelAndView.getView());
        assertEquals(expReportList, modelAndView.getAttributes().get("reports"));
        assertFalse(modelAndView.isRedirect());
    }


}

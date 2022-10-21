package cargo.delivery.epam.com.project.logic.controllers;

import cargo.delivery.epam.com.project.infrastructure.web.ModelAndView;
import cargo.delivery.epam.com.project.infrastructure.web.PaginationLinksBuilder;
import cargo.delivery.epam.com.project.infrastructure.web.RequestParameterMapper;
import cargo.delivery.epam.com.project.logic.entity.Report;
import cargo.delivery.epam.com.project.logic.entity.dto.FilteringDto;
import cargo.delivery.epam.com.project.logic.services.ManagerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ManagerController {
    private final ManagerService managerService;
    private final RequestParameterMapper requestParameterMapper;
    private final PaginationLinksBuilder paginationLinksBuilder;


    // /app/cargo/manager/getAllOrders
    public ModelAndView getAllOrders(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        int page = Integer.parseInt(request.getParameter("page"));
        List<Report> reportList = managerService.getAllOrders(page);
        int countPages = managerService.getCountPagesAllOrders();
        List<String> paginationLinks = paginationLinksBuilder.buildLinks(request, countPages);
        modelAndView.setView("/manager/allOrders.jsp");
        modelAndView.addAttribute("reports", reportList);
        modelAndView.addAttribute("paginationLinks", paginationLinks);
        return modelAndView;
    }

    // /app/cargo/manager/getNotConfirmedOrders
    public ModelAndView getNotConfirmedOrders(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        List<Report> reportListWithNotConfirmedOrders = managerService.getNotConfirmedOrders();
        modelAndView.setView("/manager/manageOrders.jsp");
        modelAndView.addAttribute("reports", reportListWithNotConfirmedOrders);

        return modelAndView;
    }

    // /app/cargo/manager/confirmOrder
    public ModelAndView confirmOrder(HttpServletRequest request) {
        Long orderId = Long.parseLong(request.getParameter("orderId"));
        managerService.confirmOrder(orderId);
        ModelAndView modelAndView = ModelAndView.withView("/cargo/manager/getNotConfirmedOrders");
        modelAndView.setRedirect(true);
        return modelAndView;
    }

    // app/cargo/manager/allOrders/filter
    public ModelAndView filterReports(HttpServletRequest request) {
        FilteringDto dto = requestParameterMapper.handleRequest(request, FilteringDto.class);
        List<Report> reportList = managerService.filterReports(dto);
        int countPages = managerService.getCountPagesFiltered(dto);
        List<String> paginationLinks = paginationLinksBuilder.buildLinks(request, countPages);
        ModelAndView modelAndView = ModelAndView.withView("/manager/allOrders.jsp");
        modelAndView.addAttribute("reports", reportList);
        modelAndView.addAttribute("paginationLinks", paginationLinks);
        return modelAndView;
    }

    // /app/cargo/manager/getReport
    public ModelAndView getReportByDayAndDirection(HttpServletRequest request) {
        String arrivalDate = request.getParameter("arrivalDate");
        String senderCity = request.getParameter("senderCity");
        String recipientCity = request.getParameter("recipientCity");
        List<Report> reports = managerService.getReportByDayAndDirection(arrivalDate, senderCity, recipientCity);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/manager/managerHome.jsp");
        modelAndView.addAttribute("reports", reports);
        return modelAndView;
    }

}

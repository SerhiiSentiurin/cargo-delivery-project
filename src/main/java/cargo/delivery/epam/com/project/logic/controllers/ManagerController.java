package cargo.delivery.epam.com.project.logic.controllers;

import cargo.delivery.epam.com.project.infrastructure.web.ModelAndView;
import cargo.delivery.epam.com.project.infrastructure.web.RequestParameterMapper;
import cargo.delivery.epam.com.project.logic.entity.Report;
import cargo.delivery.epam.com.project.logic.entity.dto.SortingDto;
import cargo.delivery.epam.com.project.logic.services.ManagerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class ManagerController {
    private final ManagerService managerService;
    private final RequestParameterMapper requestParameterMapper;


    // /app/cargo/manager/getAllOrders
    public ModelAndView getAllOrders(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        List<Report> reportList = managerService.getAllOrders();
        modelAndView.setView("/manager/allOrders.jsp");
        modelAndView.addAttribute("reports", reportList);
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
        SortingDto dto = requestParameterMapper.handleRequest(request, SortingDto.class);
        List<Report> reportList = managerService.filterReports(dto);
        ModelAndView modelAndView = ModelAndView.withView("/manager/allOrders.jsp");
        modelAndView.addAttribute("reports", reportList);
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

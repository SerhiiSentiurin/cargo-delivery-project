package cargo.delivery.epam.com.project.logic.controllers;

import cargo.delivery.epam.com.project.infrastructure.web.ModelAndView;
import cargo.delivery.epam.com.project.infrastructure.web.RequestParameterMapper;
import cargo.delivery.epam.com.project.logic.entity.Report;
import cargo.delivery.epam.com.project.logic.services.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    private final RequestParameterMapper requestParameterMapper;


    // /app/cargo/manager/getAllOrders
    public ModelAndView getAllOrders (HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        List<Report> reportList = reportService.getAllOrders();
        modelAndView.setView("/manager/allOrders.jsp");
        modelAndView.addAttribute("reports", reportList);
        return modelAndView;
    }

}

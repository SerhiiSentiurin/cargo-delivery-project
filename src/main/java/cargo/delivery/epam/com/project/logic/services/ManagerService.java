package cargo.delivery.epam.com.project.logic.services;

import cargo.delivery.epam.com.project.infrastructure.web.exception.AppException;
import cargo.delivery.epam.com.project.logic.dao.ManagerDAO;
import cargo.delivery.epam.com.project.logic.dao.ReportFilteringDAO;
import cargo.delivery.epam.com.project.logic.entity.Report;
import cargo.delivery.epam.com.project.logic.entity.dto.SortingDto;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ManagerService {
    private final ManagerDAO managerDAO;
    private final ReportFilteringDAO reportFilteringDAO;

    public int getCountPagesFilter(SortingDto sortingDto) {
        int filterRows = reportFilteringDAO.getFilterCount(sortingDto);
        return (filterRows + 5 - 1) / 5;
    }

    public List<Report> getAllOrders() {
        List<Report> allOrders = managerDAO.getAllOrders();
        return setOrdersAndClientToReport(allOrders);
    }

    public List<Report> getNotConfirmedOrders() {
        List<Report> notConfirmedOrders = managerDAO.getNotConfirmedOrders();
        return setOrdersAndClientToReport(notConfirmedOrders);
    }

    public List<Report> filterReports(SortingDto dto) {
        List<Report> filteredReportList = reportFilteringDAO.filterReports(dto);
        return setOrdersAndClientToReport(filteredReportList);
    }

    public List<Report> getReportByDayAndDirection(String arrivalDate, String senderCity, String recipientCity) {
        List<Report> reportsByDayAndDirection = managerDAO.getReportByDayAndDirection(arrivalDate, senderCity, recipientCity);
        if (reportsByDayAndDirection.isEmpty()) {
            throw new AppException("No orders with this data! Try to enter another date or cities!");
        }
        return setOrdersAndClientToReport(reportsByDayAndDirection);
    }

    public void confirmOrder(Long orderId) {
        managerDAO.confirmOrder(orderId);
    }

    private List<Report> setOrdersAndClientToReport(List<Report> allOrders) {
        List<Report> reportList = new ArrayList<>();
        for (Report report : allOrders) {
            report.setOrder(reportFilteringDAO.getOrderById(report.getOrder().getId()));
            report.setClient(reportFilteringDAO.getClientById(report.getClient().getId()));
            reportList.add(report);
        }
        return reportList;
    }

}

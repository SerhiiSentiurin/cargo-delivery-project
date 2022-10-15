package cargo.delivery.epam.com.project.logic.services;

import cargo.delivery.epam.com.project.logic.dao.ManagerDAO;
import cargo.delivery.epam.com.project.logic.dao.SortingDAO;
import cargo.delivery.epam.com.project.logic.entity.Report;
import cargo.delivery.epam.com.project.logic.entity.dto.SortingDto;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ManagerService {
    private final ManagerDAO managerDAO;
    private final SortingDAO sortingDAO;

    public List<Report> getAllOrders(){
        List<Report> reportList = new ArrayList<>();
        List<Report> allOrders = managerDAO.getAllOrders();
        for (Report report: allOrders) {
            report.setOrder(sortingDAO.getOrderById(report.getOrder().getId()));
            report.setClient(sortingDAO.getClientById(report.getClient().getId()));
            reportList.add(report);
        }
        return reportList;
    }

    public List<Report> getNotConfirmedOrders(){
        List<Report> reportList = new ArrayList<>();
        List<Report> notConfirmedOrders = managerDAO.getNotConfirmedOrders();
        for(Report report:notConfirmedOrders){
            report.setOrder(sortingDAO.getOrderById(report.getOrder().getId()));
            report.setClient(sortingDAO.getClientById(report.getClient().getId()));
            reportList.add(report);
        }
        return reportList;
    }

        public List<Report> select(SortingDto dto){
        List<Report> reportList = new ArrayList<>();
        List<Report> sortedReportList = sortingDAO.select(dto);
        for(Report report:sortedReportList){
            report.setOrder(sortingDAO.getOrderById(report.getOrder().getId()));
            report.setClient(sortingDAO.getClientById(report.getClient().getId()));
            reportList.add(report);
        }
        return reportList;

    }

    public void confirmOrder(Long orderId){
        managerDAO.confirmOrder(orderId);
    }

}

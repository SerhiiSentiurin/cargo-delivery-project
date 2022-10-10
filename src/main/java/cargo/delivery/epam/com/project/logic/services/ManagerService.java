package cargo.delivery.epam.com.project.logic.services;

import cargo.delivery.epam.com.project.logic.dao.ClientDAO;
import cargo.delivery.epam.com.project.logic.dao.ManagerDAO;
import cargo.delivery.epam.com.project.logic.entity.Report;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ManagerService {
    private final ManagerDAO managerDAO;
    private final ClientDAO clientDAO;

    public List<Report> getAllOrders(){
        List<Report> reportList = new ArrayList<>();
        List<Report> allOrders = managerDAO.getAllOrders();
        for (Report report: allOrders) {
            report.setOrder(clientDAO.getOrderById(report.getOrder().getId()));
            report.setClient(clientDAO.getClientById(report.getClient().getId()));
            reportList.add(report);
        }
        return reportList;
    }

    public List<Report> getNotConfirmedOrders(){
        List<Report> reportList = new ArrayList<>();
        List<Report> notConfirmedOrders = managerDAO.getNotConfirmedOrders();
        for(Report report:notConfirmedOrders){
            report.setOrder(clientDAO.getOrderById(report.getOrder().getId()));
            report.setClient(clientDAO.getClientById(report.getClient().getId()));
            reportList.add(report);
        }
        return reportList;
    }

    public void confirmOrder(Long orderId){
        managerDAO.confirmOrder(orderId);
    }

}

package cargo.delivery.epam.com.project.logic.services;

import cargo.delivery.epam.com.project.logic.dao.ClientDAO;
import cargo.delivery.epam.com.project.logic.dao.ReportDAO;
import cargo.delivery.epam.com.project.logic.entity.Report;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ReportService {
    private final ReportDAO reportDAO;
    private final ClientDAO clientDAO;

    public List<Report> getAllOrders(){
        List<Report> reportList = new ArrayList<>();

        List<Report> allOrders = reportDAO.getAllOrders();
        for (Report report: allOrders) {
            report.setOrder(clientDAO.getOrderById(report.getOrder().getId()));
            report.setClient(clientDAO.getClientById(report.getClient().getId()));
            reportList.add(report);
        }
        return reportList;
    }

}

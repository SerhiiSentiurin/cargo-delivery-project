package cargo.delivery.epam.com.project.logic.services;

import cargo.delivery.epam.com.project.logic.dao.ClientDAO;
import cargo.delivery.epam.com.project.logic.dao.SortingDAO;
import cargo.delivery.epam.com.project.logic.entity.Report;
import cargo.delivery.epam.com.project.logic.entity.dto.SortingDto;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SortingService {
    private final SortingDAO sortingDAO;
    private final ClientDAO clientDAO;

    public List<Report> sort(SortingDto dto){
        List<Report> reportList = new ArrayList<>();
        List<Report> sortedReportList = sortingDAO.sort(dto);
        for(Report report:sortedReportList){
            report.setOrder(clientDAO.getOrderById(report.getOrder().getId()));
            report.setClient(clientDAO.getClientById(report.getClient().getId()));
            reportList.add(report);
        }
        return reportList;

    }
}

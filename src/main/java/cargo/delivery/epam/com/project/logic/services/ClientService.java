package cargo.delivery.epam.com.project.logic.services;

import cargo.delivery.epam.com.project.infrastructure.web.exception.AppException;
import cargo.delivery.epam.com.project.logic.dao.ClientDAO;
import cargo.delivery.epam.com.project.logic.dao.ReportFilteringDAO;
import cargo.delivery.epam.com.project.logic.entity.Client;
import cargo.delivery.epam.com.project.logic.entity.Order;
import cargo.delivery.epam.com.project.logic.entity.Report;
import cargo.delivery.epam.com.project.logic.entity.dto.ClientCreateDto;
import cargo.delivery.epam.com.project.logic.entity.dto.SortingDto;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class ClientService {
    private final ClientDAO clientDAO;
    private final ReportFilteringDAO reportFilteringDAO;

    public void createNewClient(ClientCreateDto dto) {
        clientDAO.createNewClient(dto);
    }

    public Client getClientById(Long clientId) {
        return reportFilteringDAO.getClientById(clientId);
    }

    public void topUpClientWallet(Double amountForTopUp, Long clientId) {
        Double clientAmount = reportFilteringDAO.getClientById(clientId).getAmount();
        clientDAO.topUpClientWallet(amountForTopUp, clientId, clientAmount);
    }

    public List<Report> getOrdersByClientId(Long clientId) {
        List<Report> reportsByClientId = clientDAO.getReportsByClientId(clientId);
        return setOrdersAndClientToReport(reportsByClientId);
    }

    public List<Report> filterOrders(SortingDto dto, Long clientId) {
        List<Report> sortedReportList = reportFilteringDAO.filterReports(dto).stream().filter(report -> report.getClient().getId().equals(clientId)).collect(Collectors.toList());
        return setOrdersAndClientToReport(sortedReportList);
    }

    public Order getOrderForInvoice(Long orderId) {
        return reportFilteringDAO.getOrderById(orderId);
    }

    public void payInvoice(Long orderId, Long clientId, LocalDate departureDate, LocalDate arrivalDate) {
        Double amountAfterPaid = checkWalletAmount(clientId, orderId);
        clientDAO.payInvoice(orderId, clientId, departureDate, arrivalDate, amountAfterPaid);
    }

    private Double checkWalletAmount(Long clientId, Long orderId) {
        Client client = reportFilteringDAO.getClientById(clientId);
        Order order = reportFilteringDAO.getOrderById(orderId);
        Double clientAmount = client.getAmount();
        Double orderCost = order.getInvoice().getPrice();
        if (clientAmount < orderCost) {
            throw new AppException("Not enough money!");
        }
        return clientAmount - orderCost;
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




package cargo.delivery.epam.com.project.logic.services;

import cargo.delivery.epam.com.project.infrastructure.web.exception.AppException;
import cargo.delivery.epam.com.project.logic.dao.ClientDAO;
import cargo.delivery.epam.com.project.logic.dao.SortingDAO;
import cargo.delivery.epam.com.project.logic.entity.Client;
import cargo.delivery.epam.com.project.logic.entity.Order;
import cargo.delivery.epam.com.project.logic.entity.Report;
import cargo.delivery.epam.com.project.logic.entity.dto.ClientCreateDto;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.*;


@RequiredArgsConstructor
public class ClientService {
    private final ClientDAO clientDAO;
    private final SortingDAO sortingDAO;

    public void createNewClient(ClientCreateDto dto) {
        clientDAO.createNewClient(dto);
    }

    public Client getClientById(Long clientId) {
        return sortingDAO.getClientById(clientId);
    }

    public void topUpClientWallet(Double amountForTopUp, Long clientId) {
        Double clientAmount = sortingDAO.getClientById(clientId).getAmount();
        clientDAO.topUpClientWallet(amountForTopUp, clientId, clientAmount);
    }

    public List<Report> getOrdersByClientId(Long clientId) {
        List<Report> reportList = new ArrayList<>();
        List<Report> reportsByClientId = clientDAO.getReportsByClientId(clientId);
        for (Report report : reportsByClientId) {
            report.setOrder(sortingDAO.getOrderById(report.getOrder().getId()));
            report.setClient(sortingDAO.getClientById(report.getClient().getId()));
            reportList.add(report);
        }
        return reportList;
    }

    public Order getOrderForInvoice(Long orderId) {
        return sortingDAO.getOrderById(orderId);
    }

    public void payInvoice(Long orderId, Long clientId, LocalDate departureDate, LocalDate arrivalDate) {
        Double amountAfterPaid = checkWalletAmount(clientId, orderId);
        clientDAO.payInvoice(orderId, clientId, departureDate, arrivalDate, amountAfterPaid);
    }

    private Double checkWalletAmount(Long clientId, Long orderId) {
        Client client = sortingDAO.getClientById(clientId);
        Order order = sortingDAO.getOrderById(orderId);
        Double clientAmount = client.getAmount();
        Double orderCost = order.getInvoice().getPrice();
        if (clientAmount < orderCost) {
            throw new AppException("Not enough money!");
        }
        return clientAmount - orderCost;
    }


}




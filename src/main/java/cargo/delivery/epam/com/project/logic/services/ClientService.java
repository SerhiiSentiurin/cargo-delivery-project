package cargo.delivery.epam.com.project.logic.services;

import cargo.delivery.epam.com.project.logic.dao.ClientDAO;
import cargo.delivery.epam.com.project.logic.entity.Client;
import cargo.delivery.epam.com.project.logic.entity.Order;
import cargo.delivery.epam.com.project.logic.entity.dto.ClientCreateDto;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.*;


@RequiredArgsConstructor
public class ClientService {
    private final ClientDAO clientDAO;

    public void createNewClient(ClientCreateDto dto) {
        clientDAO.createNewClient(dto);
    }

    public Client getClientById(Long clientId) {
        return clientDAO.getClientById(clientId);
    }

    public void topUpClientWallet(Double amount, Long clientId) {
        clientDAO.topUpClientWallet(amount, clientId);
    }

    public List<Order> getOrdersByClientId(Long clientId) {
        return clientDAO.getOrdersByClientId(clientId);
    }

    public Order getOrderForInvoice(Long clientId, Long orderId) {
        return clientDAO.getOrderForInvoice(clientId, orderId);
    }

    public void payInvoice(Long orderId, Long clientId, LocalDate departureDate, LocalDate arrivalDate) {
        clientDAO.payInvoice(orderId, clientId, departureDate, arrivalDate);
    }

}




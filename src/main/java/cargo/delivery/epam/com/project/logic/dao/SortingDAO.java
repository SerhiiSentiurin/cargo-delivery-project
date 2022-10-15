package cargo.delivery.epam.com.project.logic.dao;

import cargo.delivery.epam.com.project.logic.entity.*;
import cargo.delivery.epam.com.project.logic.entity.dto.SortingDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class SortingDAO {
    private final DataSource dataSource;

    @SneakyThrows
    public List<Report> select(SortingDto dto) {

        List<Report> reportList = new ArrayList<>();
        String departureDateInQuery = checkDtoDepartureDate(dto);
        String arrivalDateInQuery = checkDtoArrivalDate(dto);
        String isConformedInQuery = checkDtoIsConfirmed(dto);
        String isPaidInQuery = checkDtoIsPaid(dto);

        String sql = "select client.id, orders.id from report join client on report.client_id=client.id join user on user.id=client.id join orders on report.order_id=orders.id " +
                "join delivery on orders.delivery_id=delivery.id join invoice on orders.invoice_id=invoice.id join route on delivery.route_id=route.id " +
                "where orders.id like ? and user.login like ? and orders.type like ? and orders.weight like ? and orders.volume like ? and route.sender_city like ? " +
                "and route.recipient_city like ? and route.distance like ? and " + departureDateInQuery + " and " + arrivalDateInQuery + " and invoice.price like ? " +
                "and orders.isConfirmed " + isConformedInQuery + " and invoice.isPaid " + isPaidInQuery + " order by orders.id desc";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            checkSortingDtoToNull(preparedStatement, dto);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Report report = new Report();
                Client client = new Client();
                Order order = new Order();
                Long clientId = resultSet.getLong("client.id");
                Long orderId1 = resultSet.getLong("orders.id");
                client.setId(clientId);
                order.setId(orderId1);
                report.setClient(client);
                report.setOrder(order);
                reportList.add(report);
            }
        }
        return reportList;
    }

    @SneakyThrows
    public Client getClientById(Long clientId) {
        String sql = "SELECT amount, login FROM user join client on user.id= client.id WHERE client.id = ?";
        Client client = new Client();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                client.setId(clientId);
                client.setAmount(resultSet.getDouble("amount"));
                client.setLogin(resultSet.getString("login"));
            }
        }
        return client;
    }

    @SneakyThrows
    public Route getRouteById(Long routeId) {
        String sql = "SELECT * FROM route WHERE id = ?";
        Route route = new Route();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, routeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                route.setId(routeId);
                route.setDistance(resultSet.getDouble("distance"));
                route.setSenderCity(resultSet.getString("sender_city"));
                route.setRecipientCity(resultSet.getString("recipient_city"));
            }
        }
        return route;
    }

    @SneakyThrows
    public Delivery getDeliveryById(Long deliveryId) {
        String sql = "SELECT * FROM delivery where id = ?";
        Delivery delivery = new Delivery();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, deliveryId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                delivery.setId(deliveryId);
                if (resultSet.getDate("departure_date") == null) {
                    delivery.setDepartureDate(null);
                } else {
                    delivery.setDepartureDate(resultSet.getDate("departure_date").toLocalDate());
                }

                if (resultSet.getDate("arrival_date") == null) {
                    delivery.setArrivalDate(null);
                } else {
                    delivery.setArrivalDate(resultSet.getDate("arrival_date").toLocalDate());
                }
                Route route = getRouteById(resultSet.getLong("route_id"));
                delivery.setRoute(route);
            }
        }
        return delivery;
    }

    @SneakyThrows
    public Order getOrderById(Long orderId) {
        String sql = "select * from orders where id = ?";
        Order order = new Order();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                order.setId(orderId);
                order.setType(resultSet.getString("type"));
                order.setWeight(resultSet.getDouble("weight"));
                order.setVolume(resultSet.getDouble("volume"));
                order.setIsConfirmed(resultSet.getBoolean("isConfirmed"));
                Delivery delivery = getDeliveryById(resultSet.getLong("delivery_id"));
                Invoice invoice = getInvoiceById(resultSet.getLong("invoice_id"));
                order.setDelivery(delivery);
                order.setInvoice(invoice);
            }
        }
        return order;
    }

    @SneakyThrows
    public Invoice getInvoiceById(Long invoiceId) {
        String sql = "SELECT * FROM invoice WHERE id = ?";
        Invoice invoice = new Invoice();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, invoiceId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                invoice.setId(invoiceId);
                invoice.setPrice(resultSet.getDouble("price"));
                invoice.setIsPaid(resultSet.getBoolean("isPaid"));
            }
        }
        return invoice;
    }

    private String checkDtoIsPaid(SortingDto dto) {
        String isPaidInQuery = null;

        if (dto.getIsPaid() == null) {
            isPaidInQuery = "like '%%'";
        }
        if (dto.getIsPaid() != null) {
            isPaidInQuery = "= ?";
        }
        return isPaidInQuery;
    }

    private String checkDtoIsConfirmed(SortingDto dto) {
        String isConformedInQuery = null;

        if (dto.getIsConfirmed() == null) {
            isConformedInQuery = "like '%%'";
        }
        if (dto.getIsConfirmed() != null) {
            isConformedInQuery = "= ?";
        }
        return isConformedInQuery;
    }

    private String checkDtoArrivalDate(SortingDto dto) {
        String arrivalDateInQuery = null;
        if (dto.getArrivalDate().isEmpty()) {
            arrivalDateInQuery = "(delivery.arrival_date like '%%' or delivery.arrival_date is null)";
        }
        if (!dto.getArrivalDate().isEmpty()) {
            arrivalDateInQuery = "delivery.arrival_date = ?";
        }
        return arrivalDateInQuery;
    }

    private String checkDtoDepartureDate(SortingDto dto) {
        String departureDateInQuery = null;
        if (dto.getDepartureDate().isEmpty()) {
            departureDateInQuery = "(delivery.departure_date like '%%' or delivery.departure_date is null)";
        }
        if (!dto.getDepartureDate().isEmpty()) {
            departureDateInQuery = "delivery.departure_date = ?";
        }
        return departureDateInQuery;
    }

    @SneakyThrows
    private void checkSortingDtoToNull(PreparedStatement preparedStatement, SortingDto dto) {
        int index = 0;
        if (dto.getOrderId() == null) {
            preparedStatement.setString(++index, "%%");
        }
        if (dto.getOrderId() != null) {
            preparedStatement.setString(++index, dto.getOrderId().toString());
        }


        if (dto.getLogin() == null || dto.getLogin().isEmpty()) {
            preparedStatement.setString(++index, "%%");
        }else {
            preparedStatement.setString(++index, "%" + dto.getLogin() + "%");
        }


        if (dto.getType().isEmpty()) {
            preparedStatement.setString(++index, "%%");
        }
        if (!dto.getType().isEmpty()) {
            preparedStatement.setString(++index, "%" + dto.getType() + "%");
        }
        if (dto.getWeight() == null) {
            preparedStatement.setString(++index, "%%");
        }
        if (dto.getWeight() != null) {
            preparedStatement.setString(++index, "%" + dto.getWeight() + "%");
        }
        if (dto.getVolume() == null) {
            preparedStatement.setString(++index, "%%");
        }
        if (dto.getVolume() != null) {
            preparedStatement.setString(++index, "%" + dto.getVolume().toString() + "%");
        }
        if (dto.getSenderCity().isEmpty()) {
            preparedStatement.setString(++index, "%%");
        }
        if (!dto.getSenderCity().isEmpty()) {
            preparedStatement.setString(++index, "%" + dto.getSenderCity() + "%");
        }
        if (dto.getRecipientCity().isEmpty()) {
            preparedStatement.setString(++index, "%%");
        }
        if (!dto.getRecipientCity().isEmpty()) {
            preparedStatement.setString(++index, "%" + dto.getRecipientCity() + "%");
        }
        if (dto.getDistance() == null) {
            preparedStatement.setString(++index, "%%");
        }
        if (dto.getDistance() != null) {
            preparedStatement.setString(++index, "%" + dto.getDistance().toString() + "%");
        }
        if (!dto.getDepartureDate().isEmpty()) {
            preparedStatement.setDate(++index, Date.valueOf(dto.getDepartureDate()));
        }
        if (!dto.getArrivalDate().isEmpty()) {
            preparedStatement.setDate(++index, Date.valueOf(dto.getArrivalDate()));
        }
        if (dto.getPrice() == null) {
            preparedStatement.setString(++index, "%%");
        }
        if (dto.getPrice() != null) {
            preparedStatement.setString(++index, "%" + dto.getPrice().toString() + "%");
        }
        if (dto.getIsConfirmed() != null) {
            preparedStatement.setBoolean(++index, dto.getIsConfirmed());
        }
        if (dto.getIsPaid() != null) {
            preparedStatement.setBoolean(++index, dto.getIsPaid());
        }
    }

}
